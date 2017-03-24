import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AnchorDx_DataOrganizer_2
{
	//把IFILE(txt)文件读到ArrayList里
	public static String ReadLibfile(String filePath, ArrayList <String> IFILE){
		String Head = "";
		try {
			String encoding = "GBK";
			File file = new File(filePath);
				
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while((lineTxt = bufferedReader.readLine()) != null){
					if(lineTxt.length() != 0){	
						//判断是否为头格式行数据
						if(lineTxt.substring(0,1).equals("#") || lineTxt.substring(0,2).equals("/*") || lineTxt.substring(0,1).equals("@") ){
							Head = lineTxt;
							continue;
						}
						//String str[] = lineTxt.split("\t");
						/*if(IFILE.contains(lineTxt)){
							continue;
						}else{
							IFILE.add(lineTxt);
						}*/
						IFILE.add(lineTxt);
					}
				}
				read.close();

			}else{
				System.out.println("找不到指定的文件："+filePath);
				return "OFF";
			}
		}catch (Exception e) {
			System.out.println("读取文件内容出错："+filePath);
			e.printStackTrace();
			return "OFF";
		}
		return Head;	
	}
	
	//处理文件
	public static ArrayList<String> getFromExcel(String filename){
		String type = filename.substring(filename.lastIndexOf(".")+1);//获取文件类型
		File file = new File(filename);
		ArrayList<String> Data_list = new ArrayList<String>();
		try {
			if(type.equals("xls")){
				Data_list = readXls(file);
			}else if(type.equals("xlsx")){
				Data_list = readXlsx(file);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Data_list;
	}
	
	/**
	 * 
	 * @param wb:excel文件对象
	 */
	//读xls格式文件
	public static ArrayList<String> readXls(File file) throws Exception{
		InputStream is = new FileInputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook(is);
		//Sheet sheet = wb.getSheetAt(0);//对应excel正文对象
		
		ArrayList<String> Data_list = new ArrayList<String>();
		
		Sheet sheet = null;
		int Sheet_Num = wb.getNumberOfSheets();//获取工作薄个数
		//System.out.println(Sheet_Num);
		
		for(int numSheet = 0; numSheet < Sheet_Num; numSheet++ ){
			sheet = wb.getSheetAt(numSheet);	//获取工作薄
			String Sheet_Name = sheet.getSheetName();//获取当前工作薄名字
			//System.out.println(Sheet_Name.trim());
			if(Sheet_Name.trim().equals("DNA预文库")){
				break;
			}else{
				sheet = null;
			}
		}
		
		for(int i = sheet.getFirstRowNum()+3; i <= sheet.getLastRowNum(); i++){
			HSSFRow hssfrow = (HSSFRow) sheet.getRow(i);//获取行
			
			HSSFCell hssfcell0 = hssfrow.getCell(0);
			HSSFCell hssfcell1 = hssfrow.getCell(1);
			hssfcell0.setCellType(Cell.CELL_TYPE_STRING);
			if( (hssfcell0 != null) && (hssfcell0.getStringCellValue().trim().equals("示例")) ){
				continue;
			}else{
				if( hssfcell1 != null){
					hssfcell1.setCellType(Cell.CELL_TYPE_STRING);//设置单元格类型为String类型，以便读取时候以string类型，也可其它
					String cellValue = hssfcell1.getStringCellValue().trim();
					Data_list.add(cellValue);
					//System.out.print(cellValue);
				}
				//System.out.println();
			}
		}
		try {
			//System.out.println("+6++");
			is.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Data_list;
	}
	
	/**
	 * 
	 * @param wb:excel文件对象
	 */
	//读xlsx格式文件
	public static ArrayList<String> readXlsx(File file) throws Exception {
		
		ArrayList<String> Data_list = new ArrayList<String>();
		
		InputStream is = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		//XSSFSheet sheet = wb.getSheetAt(2);	//获取第三个工作薄
		XSSFSheet sheet = null;
		int Sheet_Num = wb.getNumberOfSheets();//获取工作薄个数
		//System.out.println(Sheet_Num);
		
		for(int numSheet = 0; numSheet < Sheet_Num; numSheet++ ){
			sheet = wb.getSheetAt(numSheet);	//获取工作薄
			String Sheet_Name = sheet.getSheetName();//获取当前工作薄名字
			if(Sheet_Name.trim().equals("DNA预文库")){
				break;
			}else{
				sheet = null;
			}
		}
		// 获取当前工作薄的每一行
		for (int i = sheet.getFirstRowNum()+3; i <= sheet.getLastRowNum(); i++) {

			XSSFRow xssfrow = sheet.getRow(i);
			
			// 获取当前工作薄的每一列
			//for (int j = xssfrow.getFirstCellNum(); j < xssfrow.getLastCellNum(); j++) {
				//XSSFCell xssfcell = xssfrow.getCell(j);
				XSSFCell xssfcell0 = xssfrow.getCell(0);
				XSSFCell xssfcell1 = xssfrow.getCell(1);
				
				if( (xssfcell0 != null) && (xssfcell0.getStringCellValue().trim().equals("示例"))  ){
					continue;
				}else{
					if( xssfcell1 != null){
						String cellValue =	xssfcell1.getStringCellValue().trim();
						Data_list.add(cellValue);
						//System.out.println(cellValue);
					}
					//System.out.println(i);
				}
		}
		try {
			is.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Data_list;
	}
	
	public static void my_mkdir( String dir_name){
		File file = new File( dir_name );
		
		//如果文件不存在，则创建
		if(!file.exists() && !file.isDirectory()){
			//System.out.println("//目录不存在");
			file.mkdirs();
		}else{
			//System.out.println("//目录已存在");
		}
	}
	
	//写文件
	public static void writefile(ArrayList<String> Data_list, String Output_File){
		try{
			FileWriter fw = new FileWriter(Output_File);
			BufferedWriter bw = new BufferedWriter(fw);
				bw.write("#Head"+"\r\n");// 往文件上写头信息
				
			for(int i = 0; i < Data_list.size(); i++){
				bw.write(Data_list.get(i)+"\r\n");
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//查找并创建对应目录(全盘模式)
	@SuppressWarnings("null")
	public static int AllCopydirAndReanExecl( File des_file, String Mkdir_Path, String excel_part_name )
	{	
		//ArrayList <String> dir_name = new ArrayList<String>();
		//File des_file = new File( dir_path );
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		String Date = formatter_Date.format(now.getTime());
		// 判断目录下是不是空的
		if( des_file == null ) {
			System.out.println("该目录为空："+des_file.getName());
			return -1;
		}else{
			for (File pathname : des_file.listFiles())
			{
				if (pathname.isDirectory()) { //如果是目录
						
					//在指定路径下创建对应目录
					String dir_name = pathname.getName();
					String dir_path_name = Mkdir_Path + dir_name+"/Master";
					my_mkdir( dir_path_name );
					//System.out.println(dir_path_name);
						
					for (File porject_name : pathname.listFiles())
					{
						if (porject_name.isFile()) { //如果是文件
								
							//String this_excel_name = dir_name + excel_part_name;
							//System.out.println(this_excel_name);
							String Suffix = porject_name.getName().substring(porject_name.getName().lastIndexOf(".")); //获取后缀名
							String Remove_suffix =  porject_name.getName().replaceAll(Suffix, ""); //去除后缀名
							//System.out.println(Remove_suffix);
							if( Remove_suffix.contains(excel_part_name) && !Remove_suffix.contains("~$") ){
								//源文件
								//String Source_File = porject_name.getParent() + "\\" + this_excel_name;
								String Source_File = porject_name.getParent() + "/"+ porject_name.getName();
								String Output_File = dir_path_name+"/"+Remove_suffix+"_"+Date+"_All_"+".txt";
								String Source_Output_File = "Master/"+Remove_suffix+"_"+Date+"_All_"+".txt";
								//String Output_File = dir_path_name+"\\"+Remove_suffix+".txt";
								//System.out.println(Source_File);
								ArrayList<String> Data_list = getFromExcel(Source_File); //读excel表
								writefile( Data_list, Output_File);
								
								//做链接
								try{
									String Link_File_All =  Mkdir_Path + dir_name +"/"+Remove_suffix+"_All_"+".txt";
									String cmd_All = "ln -s -f " + Source_Output_File + " " + Link_File_All;
									Runtime.getRuntime().exec(cmd_All);//链接全盘名单
								}catch(Exception e){
									System.out.println("链接出错！");	
								}	
							}else{
								continue;
							}		
							//System.out.println();	
						} else {
							continue;
						}
					}	
				} else {
					continue;
				}
			}
		}
		return 0;
	}
	
	//查找并创建对应目录(更新模式)
	@SuppressWarnings("null")
	public static int UpdataCopydirAndReanExecl( File des_file, String Mkdir_Path, String excel_part_name )
	{	
		//ArrayList <String> dir_name = new ArrayList<String>();
		//File des_file = new File( dir_path );
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		String Date = formatter_Date.format(now.getTime());
		
		ArrayList <String> OldDatalist = new ArrayList <String>();
		ArrayList <String> UpdataDatalist = new ArrayList <String>();
		
		// 判断目录下是不是空的
		if( des_file == null ) {
			System.out.println("该目录为空："+des_file.getName());
			return -1;
		}else{
			for (File pathname : des_file.listFiles())
			{
				if (pathname.isDirectory()) { //如果是目录
						
					//在指定路径下创建对应目录
					String dir_name = pathname.getName();
					String dir_path_name = Mkdir_Path + dir_name + "/Master";
					my_mkdir( dir_path_name );
					//System.out.println(dir_path_name);
						
					for (File porject_name : pathname.listFiles())
					{
						if (porject_name.isFile()) { //如果是文件
								
							//String this_excel_name = dir_name + excel_part_name;
							//System.out.println(this_excel_name);
							String Suffix = porject_name.getName().substring(porject_name.getName().lastIndexOf(".")); //获取后缀名
							String Remove_suffix =  porject_name.getName().replaceAll(Suffix, ""); //去除后缀名
							//System.out.println(Remove_suffix);
							//if( Remove_suffix.equals(this_excel_name) ){
							if( Remove_suffix.contains(excel_part_name)  && !Remove_suffix.contains("~$") ){
								//源文件
								//String Source_File = porject_name.getParent() + "\\" + this_excel_name;
								String Source_File = porject_name.getParent() + "/"+ porject_name.getName();
								String Output_File = dir_path_name+"/"+Remove_suffix+"_"+Date+"_All_"+".txt";
								String LastTime_Output_File = Mkdir_Path + dir_name +"/"+Remove_suffix+"_All_"+".txt";
								String Updata_File = dir_path_name+"/"+Remove_suffix+"_"+Date+"_Updata_"+".txt";
								String Source_All_Output_File = "Master/"+Remove_suffix+"_"+Date+"_All_"+".txt";
								String Source_Updata_Output_File = "Master/"+Remove_suffix+"_"+Date+"_Updata_"+".txt";
								//System.out.println(Source_File);
								OldDatalist.clear();
								UpdataDatalist.clear();
								ReadLibfile(LastTime_Output_File, OldDatalist);//读上次更新的表数据
								ArrayList<String> Data_list = getFromExcel(Source_File); //读excel表
								writefile( Data_list, Output_File);
								for(int i = OldDatalist.size(); i < Data_list.size(); i++){
									UpdataDatalist.add(Data_list.get(i));
								}
								writefile( UpdataDatalist, Updata_File);
									
								//做链接
								try{
									String Link_File_All =  Mkdir_Path + dir_name +"/"+Remove_suffix+"_All_"+".txt";
									String cmd_All = "ln -s -f " + Source_All_Output_File + " " + Link_File_All;
									Runtime.getRuntime().exec(cmd_All);//链接全盘名单
										
									String Link_File_Updata =  Mkdir_Path + dir_name +"/"+Remove_suffix+"_Updata_"+".txt";
									String cmd_Updata = "ln -s -f " + Source_Updata_Output_File + " " + Link_File_Updata;
									Runtime.getRuntime().exec(cmd_Updata);//链接更新名单
										
								}catch(Exception e){
									System.out.println("链接出错！");	
								}
									
							}else{
								continue;
							}	
							//System.out.println();	
						} else {
							continue;
						}
					}
				} else {
					continue;
				}
			}
		}
		return 0;
	}
	
	/*public static int CopyExcel(){
		try{
			String cmd_Sample_statistics = "sh ./CopyExcel.sh";
			//String cmd_Sample_statistics[] = {"sh", "。/CopyExcel.sh"};
			Runtime.getRuntime().exec(cmd_Sample_statistics);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}*/
	
	public static int CopyExcel(){
		try{
			//String cmd_Sample_statistics = "sh ./CopyExcel.sh";		
			String cmd_Sample_statistics[] = {"rsync", "-aP", "--include=*/","--include=**/*样本处理追踪表*.xls*", "--exclude=*", "zhirong_lu@192.192.192.220:/wdmycloud/anchordx_cloud/杨莹莹/基准所有项目收样信息表样本处理追踪表", "."};
			//Runtime.getRuntime().exec(cmd_Sample_statistics);
			Process process = Runtime.getRuntime().exec(cmd_Sample_statistics);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	//上传文件
	public static void Upload_File(String PutPath)
	{
		//String Account = "admin";
		//String Password = "admin123456";
		String cmd = "/opt/local/bin/python35/python /var/script/alan/10k_api_script/white_black_collections.py -path " + PutPath;
		//System.out.println(cmd);
		try{
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
   public static void main(String[] args) throws Exception {
	   
	   	System.out.println();
		Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_star = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("程序开始时间: "+now_star.getTime());
		System.out.println("程序开始时间: "+formatter_star.format(now_star.getTime()));
		System.out.println("===============================================");
		System.out.println("Version: AnchorDx_DataOrganizer.V1.1.4");
		System.out.println("***********************************************");
		
		//复制zhirong_lu@192.192.192.200:/wdmycloud/anchordx_cloud/杨莹莹/基准所有项目收样信息表样本处理追踪表到本地
		int tag = CopyExcel();
		System.out.println("CopyExcel finish!");
		//Thread.sleep(10000);
	   
		//String porject_Path = "/home/zhirong_lu/code/test/5/基准所有项目收样信息表样本处理追踪表"; // 源文件路径
		//String Mkdir_Path = "/home/zhirong_lu/code/test/5/Projects/"; //目标路径
		String porject_Path = "./基准所有项目收样信息表样本处理追踪表"; // 源文件路径
		String Mkdir_Path = "./Projects/"; //目标路径
		String excel_part_name = "样本处理追踪表v1_广州基准医疗";
		int Pattern = 0;
		File des_file = null;
		int time = 0;
		while(true){
			des_file = new File( porject_Path );
			if(des_file.exists()){
				break;
			}else if(time == 100) {
				System.out.println("对不起，由于在1000秒内无法获取到“./基准所有项目收样信息表样本处理追踪表”目录，因此结束程序！！！");
				System.out.println();
				System.out.println("===============================================");
				Calendar now_end = Calendar.getInstance();
				SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("程序结束时间: "+now_end.getTime());
				System.out.println("程序结束时间: "+formatter_end.format(now_end.getTime()));
				return;
			}else{
				Thread.sleep(10000);
				time ++;
				continue;
			}
		}
		
		int args_len = args.length;//输入参数长度
		for(int len = 0; len < args_len; len++){
			if(args[len].equals("-O") || args[len].equals("-o")){
				Mkdir_Path = args[len+1] + "/";
			}else if(args[len].equals("-M") || args[len].equals("-m")){
				Pattern = Integer.valueOf(args[len+1]);
			}
		}
		//根据上面操作所得数据生成对应文件
		if(tag == 0){
			int UF = 1;
			
			if(Pattern == 0){
				UF = AllCopydirAndReanExecl(des_file, Mkdir_Path, excel_part_name);//全盘模式
			}else{
				UF = UpdataCopydirAndReanExecl(des_file, Mkdir_Path, excel_part_name);//更新模式
			}
			System.out.println("CopydirAndReanExecl finish!");
			
			if(UF == 0){
				//查找
				AnchorDx_CollectData_SearchFiles.Main_Fun(Mkdir_Path, Pattern);
				System.out.println("AnchorDx_CollectData_SearchFiles finish!");
			}
		}
		
		Thread.sleep(3000);
		Upload_File(Mkdir_Path);//上传文件
		
		System.out.println();
		System.out.println("===============================================");
		Calendar now_end = Calendar.getInstance();
		SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("程序结束时间: "+now_end.getTime());
		System.out.println("程序结束时间: "+formatter_end.format(now_end.getTime()));
   }
}

//查找文件类
class AnchorDx_CollectData_SearchFiles
{
	//把IFILE(txt)文件读到ArrayList里
	public static String ReadLibfile(String filePath, ArrayList <String> IFILE){
		String Head = "";
		try {
			String encoding = "GBK";
			File file = new File(filePath);
					
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while((lineTxt = bufferedReader.readLine()) != null){	
					//判断是否为头格式行数据
					if(lineTxt.length() != 0){
						//System.out.println("lineTxt: "+lineTxt+"==="+lineTxt.length());
						if( lineTxt.substring(0,1).equals("#") || lineTxt.substring(0,2).equals("/*") || lineTxt.substring(0,1).equals("@") ){
							Head = lineTxt;
							continue;
						}

						//String str[] = lineTxt.split("\t");
						if(IFILE.contains(lineTxt)){
							continue;
						}else{
							IFILE.add(lineTxt);
						}
					}
				}
				read.close();
			}
		}catch (Exception e) {
			System.out.println("读取文件内容出错："+filePath);
			e.printStackTrace();
			return "OFF";
		}
		return Head;	
	}	
	//判断一个Linux下的文件是否为链接文件，是返回true ,否则返回false
	public static boolean isLink(File file) {
		 String cPath = "";
		 try {
			  cPath = file.getCanonicalPath();
		} catch (Exception ex) {
			System.out.println("文件异常："+file.getAbsolutePath());
		}
		return !cPath.equals(file.getAbsolutePath());
	}
	
	//调用linux命令获取符合要求的文件列表(跳过链接文件)
	public static void Linux_Cmd(String Path, String Extension, ArrayList <String> list){
		try{
			String tar = "*" + Extension;
			String cmd = "find " + Path + " -type f -name " + tar;
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				list.add(line);
			}
		}catch(Exception e){
			System.out.println("linux命令异常！");
		}
	}
	
	//在列表中匹配
	public static ArrayList<String> getContainssWordFile( ArrayList<String> list_Path, String SampleID, String Extension){	
		ArrayList<String> list = new ArrayList<String>();
		for( int x = 0; x < list_Path.size(); x++ ){
			File file = new File(list_Path.get(x));		
			String Folder = file.getParent();//获取文件的绝对路径			
			String FileName = file.getName();//文件文件名
			if(FileName.contains(SampleID)){
				String str = SampleID+"\t"+Extension+"\t"+Folder+"\t"+FileName;
				list.add(str);
			}else{
				continue;
			}
		}
		return list;
	}
	
	//查找#SampleID文件列表
	@SuppressWarnings("null")
	public static ArrayList<String> Search_SampleID_list( File des_file, String SampleID_File_part_name )
	{	
		ArrayList <String> SampleID_list = new ArrayList<String>();
		// 判断目录下是不是空的
		if( des_file == null ) {
			System.out.println("该目录为空："+des_file.getName());
			return null;
		}else{
			for (File pathname : des_file.listFiles())
			{
				if (pathname.isDirectory()) { //如果是目录
						
					//String dir_name = pathname.getName();
					//String SampleID_File_All_name = dir_name + SampleID_File_part_name;
						
					//String Suffix = porject_name.getName().substring(porject_name.getName().lastIndexOf(".")); //获取后缀名
					//String Remove_suffix =  porject_name.getName().replaceAll(Suffix, ""); //去除后缀名
						
					//String SampleID_File_All_name =  Mkdir_Path + dir_name +"/"+Remove_suffix+"_All_"+".txt";
					for (File porject_name : pathname.listFiles())
					{
						if (porject_name.isFile()) { //如果是文件
								
							String this_SampleID_name = porject_name.getName();
							if( this_SampleID_name.contains(SampleID_File_part_name) ){
								String SampleID_Path = porject_name.getParent() + "/" + this_SampleID_name;//源文件
								SampleID_list.add(SampleID_Path);
								//System.out.println("SampleID_Path："+SampleID_Path);
							}else{
								continue;
							}
						} else {
							continue;
						}
					}						
				} else {
					continue;
				}
			}
		}
		return SampleID_list;
	}
	
	//读取修改时间的方法  
	public static String getModifiedTime(String file){
		File f = new File(file);
		Calendar cal = Calendar.getInstance();
		long time = f.lastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		cal.setTimeInMillis(time);
		return formatter.format(cal.getTime());
	}
	
	//返回文件查找结果
	public static ArrayList<String> Return_FilePath(ArrayList <String> IFILE_List, ArrayList <String> Search_Path, ArrayList<String> Day_list, String Extension_Data, String Extension)
	{
		ArrayList <String> FileName_List =  new ArrayList <String>();
		ArrayList<String> list_Path = new ArrayList<String>();
		ArrayList<String> list = new ArrayList <String>();
		String SampleID = "";

		//读取文件路径信息到列表
		list_Path.clear();
		for(int y=0; y<Search_Path.size(); y++){
			Linux_Cmd(Search_Path.get(y), Extension_Data, list_Path);
		}
		for(int i=0; i<IFILE_List.size(); i++){
			SampleID = IFILE_List.get(i);
			list.clear();
			int log = 0;
						
			list = getContainssWordFile( list_Path, SampleID, Extension);
			if( list.size() == 0){
				String str = SampleID+"\t"+Extension+"\t"+"NA"+"\t"+"NA";
				FileName_List.add(str);
						
				String day_data = SampleID + "\t" + "NA";
				Day_list.add(day_data);
			}else{
				for (int x = 0; x < list.size(); x++){
					//FileName_List.add(list.get(x));
					if( FileName_List.contains(list.get(x)) ){
						continue;
					}else{
						FileName_List.add(list.get(x));
						if(log == 0){
							String strr[] = list.get(x).split("\t");
							String file = strr[2] + "/" + strr[3];
							String Day = getModifiedTime(file);
							String day_data = SampleID + "\t" + Day;
							Day_list.add(day_data);
							log ++;
						}
						//System.out.println(list.get(x));
						continue;
					}
				}
			}
		}	
		return FileName_List;
	}
	
	public static void write_show( String Head, String Output_File, List<String> list, int log){
		
		//如果用户不输入OutPutFilePath，则按格式输出到终端
		if( Output_File == null ){
			//头信息
			if(log == 0){
				System.out.println();
				if( Head == null ){
						System.out.println("#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName");
				}else{
						System.out.println(Head+"\t"+"Folder"+"\t"+"FileName");
				}
			}
			for (int x = 0; x < list.size(); x++){
				System.out.println(list.get(x));
			}
			//System.out.println();
		}else{ //如果用户输入OutPutFilePath，则按格式写到OutPutFilePath.txt文件里
			try {
				/*FileWriter fw = null;
				if(log == 0){
					fw = new FileWriter(Output_File);//每次覆盖以前数据
				}else{
					fw = new FileWriter(Output_File, true);//每次追加数据
				}*/
				FileWriter fw = new FileWriter(Output_File);//每次覆盖以前数据;
				BufferedWriter bw = new BufferedWriter(fw);
				if(log == 0){
					if( Head != null){
						bw.write(Head+"\t"+"Folder"+"\t"+"FileName"+"\r\n");// 往文件上写头信息
					}else{
						String FileHead ="#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName";
						bw.write(FileHead+"\r\n");
					}
				}	
				for(int i = 0; i < list.size(); i++){
					bw.write(list.get(i)+"\r\n");
				}
				bw.close();
				fw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//创建目录
	public static void my_mkdir( String dir_name){
		File file = new File( dir_name );
		
		//如果文件不存在，则创建
		if(!file.exists() && !file.isDirectory()){
			//System.out.println("//目录不存在");
			file.mkdirs();
		}
	}
	
	public static void Main_Fun(String SampleID_Path, int Pattern)
	{
		//ArrayList <String> Extension_List = new ArrayList <String>();//#Extension
		//String Extension_Path = "/home/zhirong_lu/code/test/5/Extension.txt";
		//String SampleID_Path = "./Projects/";
		File des_file = new File(SampleID_Path);
		String SampleID_File_part_name = "样本处理追踪表v1_广州基准医疗_All_.txt";
		//String SampleID_File_part_name = "样本处理追踪表v1_广州基准医疗.txt";
		
		System.out.println();
		/*Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_star = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("程序开始时间: "+now_star.getTime());
		System.out.println("程序开始时间: "+formatter_star.format(now_star.getTime()));*/
			
		ArrayList<String> SampleID_File_list = Search_SampleID_list( des_file, SampleID_File_part_name );//获取SampleID文件列表
		//System.out.println("===============================================");
		//AnchorDx_CollectData_SearchFiles.ReadLibfile(Extension_Path, Extension_List);//读取Extension文件
		/*for( int i = 0; i < SampleID_File_list.size(); i++ ){
			//System.out.println(Thread.currentThread().getName()+"开始");//打印开始标记
			WorkThread thread = new WorkThread(SampleID_File_list.get(i));
			thread.start();
		}
		while(true){//等待所有子线程执行完  
			if(!WorkThread.hasThreadRunning()){
				break;
			}
			try{
				Thread.sleep(500);
			}catch(Exception e){
				System.out.println("主线程睡醒出错！ ");
			}
		}*/
		
		ExecutorService exe = Executors.newFixedThreadPool(20);//设置线程池最大线程数为20
		for( int i = 0; i < SampleID_File_list.size(); i++ ){
			//System.out.println(SampleID_File_list.get(i));
			exe.execute( new WorkThread(SampleID_File_list.get(i), Pattern) );//向线程池提交任务
		}
        exe.shutdown();//关闭线程池
		while (true)
        {
            if (exe.isTerminated()) //先让所有的子线程运行完，再运行主线程
            {
                //System.out.println("结束了");
                String cmd = "rm -r ./基准所有项目收样信息表样本处理追踪表";
                try {
                	Runtime.getRuntime().exec(cmd);
                }catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                break;
            }
            try{
				Thread.sleep(500);
			}catch(Exception e){
				System.out.println("主线程睡醒出错！ ");
			}
        }
			
		/*Calendar now_end = Calendar.getInstance();
		SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println();
		System.out.println("==============================================");
		System.out.println("程序结束时间: "+now_end.getTime());
		System.out.println("程序结束时间: "+formatter_end.format(now_end.getTime()));*/
		System.out.println();
	}
}
//线程类
class WorkThread extends Thread {
	private String SampleID_File_list_get = null;
	private String Output_File_Black = null;
	private String Output_File_White = null;
	private String Link_File_Black = null;
	private String Link_File_White = null;
	private String Source_File_Black = null;
	private String Source_File_White = null;
	private String Sample_statistics = null;
	private String IFILE_Path = null;
	private ArrayList <String> Search_Path =  new ArrayList <String>();
	private ArrayList <String> IFILE_List =  new ArrayList <String>();
	private ArrayList<String> NoSearch_FilePath_list = new ArrayList <String>();
	private ArrayList<String> list = new ArrayList <String>();
	private ArrayList<String> Black_List = new ArrayList <String>();
	private ArrayList<String> White_List = new ArrayList <String>();
	private ArrayList <String> White_All_Data = new ArrayList<String>();
	private ArrayList <String> Black_All_Data = new ArrayList<String>();
	private ArrayList<String> Updata_SampleID_list = new ArrayList <String>();
	private ArrayList <String> Extension_List = new ArrayList <String>();//#Extension
	private static List<Thread> runningThreads = new ArrayList<Thread>();
	private String Extension_Path = null;
	private String Path1 = null;
	private String Path2 = null;
	private String Path3 = null;
	private String Head = "#SampleID"+"\t"+"Extension";
	private String SampleID_File_part_name = null;
	private int Pattern = 0;
	public WorkThread(String SampleID_File_list_get, int Pattern) {
        super();
        this.SampleID_File_list_get = SampleID_File_list_get;
		this.Extension_Path = "./Extension.txt";
		this.Path1 = "/Src_Data1/nextseq500/outputdata/";
		this.Path2 = "/Src_Data1/x10/outputdata/";
		this.Path3 = "/Src_Data1/analysis/Ironman/";
		this.SampleID_File_part_name = "样本处理追踪表v1_广州基准医疗.txt";
		this.Pattern = Pattern;
    }
	public void run() {
		regist(this);//线程开始时注册
		AnchorDx_CollectData_SearchFiles.ReadLibfile(Extension_Path, Extension_List);
				
		File SampleID_File = new File(SampleID_File_list_get);
		File SampleID_File_Path = new File(SampleID_File.getParent());
		String SampleID_PorjectName = SampleID_File_Path.getName();
		String Output_Sub_Directory = null;
		String Extension = null;
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		String Date = formatter_Date.format(now.getTime());
		
		int logg1 = 0;
		int logg2 = 0;
		int logg3 = 0;
		int logg4 = 0;
		int logg5 = 0;
		int logg6 = 0;
		int logg7 = 0;
		int logg8 = 0;
		int logg9 = 0;
		int logg10 = 0;	
		int loog = 0;
		
		Calendar now_porject = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(SampleID_PorjectName+"项目开始时间："+ formatter.format(now_porject.getTime()));
				
		String SampleID_Path = SampleID_File.getParent();
		String Master = SampleID_File.getParent() + "/" + "Master";
		AnchorDx_CollectData_SearchFiles.my_mkdir( Master );//创建Master目录
				
		//返回文件查找结果
		for(int x = 0; x < Extension_List.size(); x++ ){
			Search_Path.clear();
			list.clear();
			IFILE_List.clear();
					
			if(Extension_List.get(x).equals("R1_001.fastq.gz") || Extension_List.get(x).equals("R2_001.fastq.gz")){
				Output_Sub_Directory = "RawFastq";
				Extension = "R[1-2]_001.fastq.gz";
				Search_Path.add(Path1);
				Search_Path.add(Path2);
				if(logg1 == 0){
					Updata_SampleID_list.clear();
					White_All_Data.clear();
					Black_All_Data.clear();
				}
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg1, loog, Extension, Pattern );
				logg1++;
				if(logg1 == 2){
					Updata_SampleID_list.clear();
					White_All_Data.clear();
					Black_All_Data.clear();
				}
				loog++;
			}else if(Extension_List.get(x).equals("R1_001.clean.fastq.gz") || Extension_List.get(x).equals("R2_001.clean.fastq.gz")){
				Output_Sub_Directory = "CleanFastq";
				Extension = "R[1-2]_001.clean.fastq.gz";
				Search_Path.add(Path3);
				if(logg2 == 0){
					Updata_SampleID_list.clear();
					White_All_Data.clear();
					Black_All_Data.clear();
				}
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg2, loog, Extension, Pattern );
				logg2++;
				if(logg2 == 2){
					Updata_SampleID_list.clear();
					White_All_Data.clear();
					Black_All_Data.clear();
				}
				loog++;
			}else if(Extension_List.get(x).equals("sorted.deduplicated.bam")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "ProcessedBam";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg3, loog, Extension, Pattern );
				logg3++;
				loog++;
			}else if(Extension_List.get(x).equals("deduplicated_splitting_report.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "BismarkReport";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg4, loog, Extension, Pattern );
				logg4++;
				loog++;
			}else if(Extension_List.get(x).equals("sorted.bam.insertSize.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "RawHsmetrics";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg5, loog, Extension, Pattern );
				logg5++;
				loog++;
			}else if(Extension_List.get(x).equals("sorted.deduplicated.bam.hsmetrics.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "DedupHsmetrics";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg6, loog, Extension, Pattern );
				logg6++;
				loog++;
			}else if(Extension_List.get(x).equals("sorted.deduplicated.bam.insertSize.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "InsertSize";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg7, loog, Extension, Pattern );
				logg7++;
				loog++;
			}else if(Extension_List.get(x).equals("sorted.deduplicated.bam.perTarget.coverage")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "TargetCoverage";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg8, loog, Extension, Pattern );
				logg8++;
				loog++;
			}else if(Extension_List.get(x).equals("LCclassification_res.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "LCclassification";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg9, loog, Extension, Pattern );
				logg9++;
				loog++;
			}else if(Extension_List.get(x).equals("CRclassification_res.txt")){
				Extension = Extension_List.get(x);
				Output_Sub_Directory = "CRclassification";
				Search_Path.add(Path3);
				Updata_SampleID_list.clear();
				White_All_Data.clear();
				Black_All_Data.clear();
				this.Show_Data(SampleID_Path, Extension_List.get(x), SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, logg10, loog, Extension, Pattern );
				logg10++;
				loog++;
			}		
		}
		unRegist(this);//线程结束时取消注册				
	}
	//注册线程
	public static void regist(Thread t){
		synchronized(runningThreads){
			runningThreads.add(t);
		}
	}
	//取消注册
	public static void unRegist(Thread t){
		synchronized(runningThreads){
			runningThreads.remove(t);
		}
	}
	//通过判断runningThreads是否为空就能知道是否还有线程未执行完
	public static boolean hasThreadRunning(){
		return(runningThreads.size() > 0);
	}
	
	//写文件统计表
	public static void Write_Sample_statistics(String Sample_statistics_head, String Output_File, ArrayList<String> list, int log ){

		String encoding = "GBK";
		File file = new File(Output_File);
		ArrayList<String> Day_list = new ArrayList<String>();
		try {
			if(log == 0){
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
				writer.write("#样本名" + "\t" + Sample_statistics_head + "\t" + "Reserve" + "\r\n");
				for(int i = 0; i < list.size(); i++){
					writer.write(list.get(i) + "\t" + "Reserve" + "\r\n");
				}
				writer.close();
			}else{
				if(file.isFile() && file.exists()){ //判断文件是否存在
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
					String lineTxt = null;

					while((lineTxt = reader.readLine()) != null){
						Day_list.add(lineTxt);	
					}
					reader.close();
				}else{
					System.out.println("找不到指定的文件："+Output_File);
					return;
				}
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
				for(int i = 0; i < Day_list.size(); i++){
					if(i == 0){
						String str0[] = Day_list.get(i).split("\t");
						String Data = null;
						for(int y = 0; y < str0.length-1; y++){
							if(y == 0){
								Data = str0[y];
							}else{
								Data += "\t" + str0[y];
							}
						}
						writer.write(Data + "\t" + Sample_statistics_head + "\t" + "Reserve" + "\r\n");
					}else{
						//writer.write(Day_list.get(i)+ "\t" + list.get(i-1) + "\r\n");
						String str[] = list.get(i-1).split("\t");
						String str1[] = Day_list.get(i).split("\t");
						String Data = null;
						for(int y = 0; y < str1.length-1; y++){
							if(y == 0){
								Data = str1[y];
							}else{
								Data += "\t" + str1[y];
							}
						}
						writer.write(Data + "\t" + str[1] + "\t" + "Reserve" + "\r\n");
					}
				}
				writer.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//写数据
	public static void TwoExtension_write_show( String Head, String Output_File, ArrayList<String> list, int log, String Extension){
		
		ArrayList<String> Datalist = new ArrayList<String>();
		String encoding = "GBK";
		//如果用户不输入OutPutFilePath，则按格式输出到终端
		if( Output_File == null ){
			//头信息
			if(log == 0){
				System.out.println();
				if( Head == null ){
						System.out.println("#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName");
				}else{
						System.out.println(Head+"\t"+"Folder"+"\t"+"FileName");
				}
				for (int x = 0; x < list.size(); x++){
					System.out.println(list.get(x));
				}
			}else{
				System.out.println();
				if( Head == null ){
					System.out.println("#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2");
				}else{
					System.out.println(Head+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2");
				}
			}
		}else{ //如果用户输入OutPutFilePath，则按格式写到OutPutFilePath.txt文件里
			try {
				/*FileWriter fw = null;
				if(log == 0){
					fw = new FileWriter(Output_File);//每次覆盖以前数据
				}else{
					fw = new FileWriter(Output_File, true);//每次追加数据
				}*/
				if(log == 0){
					FileWriter fw = new FileWriter(Output_File);//每次覆盖以前数据
					BufferedWriter bw = new BufferedWriter(fw);
					if( Head != null){
						bw.write(Head+"\t"+"Folder"+"\t"+"FileName"+"\r\n");// 往文件上写头信息
					}else{
						String FileHead ="#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName";
						bw.write(FileHead+"\r\n");
					}
					if(list.size() != 0){
						for(int i = 0; i < list.size(); i++){
							bw.write(list.get(i)+"\r\n");
						}
					}else{
						return;
					}
					bw.close();
					fw.close();
				}else{
					File Infile = new File(Output_File);
					
					if(Infile.isFile() && Infile.exists()){ //判断文件是否存在
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Infile), encoding));
						String lineTxt = null;
						while((lineTxt = reader.readLine()) != null){
							
						//判断是否为头格式行数据
						if(lineTxt.substring(0,1).equals("#") || lineTxt.substring(0,2).equals("/*") || lineTxt.substring(0,1).equals("@") ){
							//Head = lineTxt;
							continue;
						}
							Datalist.add(lineTxt);
						}
						reader.close();

					}else{
						System.out.println("找不到指定的文件："+Output_File);
						return;
					}
					FileWriter ffw = new FileWriter(Output_File);//每次覆盖以前数据
					BufferedWriter bbw = new BufferedWriter(ffw);
					if( Head != null){
						bbw.write(Head+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2"+"\r\n");// 往文件上写头信息
					}else{
						String FileHead ="#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2";
						bbw.write(FileHead+"\r\n");
					}
					if(list.size() != 0){
						for(int i = 0; i < Datalist.size(); i++){
							String str1[] = Datalist.get(i).split("\t");
							/*String str2[] = list.get(i).split("\t");
							if(str1[0].equals(str2[0])){
								bbw.write(Datalist.get(i)+"\t"+str2[str2.length-1]+"\r\n");
							}else{
								continue;
							}*/
							for(int j = 0; j < list.size(); j++){
								String str2[] = list.get(j).split("\t");
								if(str1[0].equals(str2[0])){
									bbw.write(Datalist.get(i)+"\t"+str2[str2.length-1]+"\r\n");
								}else{
									continue;
								}
							}
						}
					}else{
						for(int i = 0; i < Datalist.size(); i++){
							bbw.write(Datalist.get(i)+"\r\n");
						}
					}
					bbw.close();
					ffw.close();
				}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//写数据
		public static void Updata_TwoExtension_write_show( String Head, String Input_File, String Output_File, ArrayList<String> list, int log, String Extension){
			
			ArrayList<String> Datalist = new ArrayList<String>();
			String encoding = "GBK";
			//如果用户不输入OutPutFilePath，则按格式输出到终端
			if( Output_File == null ){
				//头信息
				if(log == 0){
					System.out.println();
					if( Head == null ){
							System.out.println("#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName");
					}else{
							System.out.println(Head+"\t"+"Folder"+"\t"+"FileName");
					}
					for (int x = 0; x < list.size(); x++){
						System.out.println(list.get(x));
					}
				}else{
					System.out.println();
					if( Head == null ){
						System.out.println("#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2");
					}else{
						System.out.println(Head+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2");
					}
				}
			}else{ //如果用户输入OutPutFilePath，则按格式写到OutPutFilePath.txt文件里
				try {
					File Infile = new File(Input_File);
					
					if(Infile.isFile() && Infile.exists()){ //判断文件是否存在
						BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Infile), encoding));
						String lineTxt = null;
						while((lineTxt = reader.readLine()) != null){
							
						//判断是否为头格式行数据
						if(lineTxt.substring(0,1).equals("#") || lineTxt.substring(0,2).equals("/*") || lineTxt.substring(0,1).equals("@") ){
							//Head = lineTxt;
							continue;
						}
							Datalist.add(lineTxt);
						}
						reader.close();

					}else{
						System.out.println("找不到指定的文件："+Input_File);
						return;
					}
					
					if(log == 0){
						FileWriter fw = new FileWriter(Output_File);//每次覆盖以前数据
						BufferedWriter bw = new BufferedWriter(fw);
						if( Head != null){
							bw.write(Head+"\t"+"Folder"+"\t"+"FileName"+"\r\n");// 往文件上写头信息
						}else{
							String FileHead ="#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName";
							bw.write(FileHead+"\r\n");
						}
						if(list.size() != 0){
							for(int i = 0; i < Datalist.size(); i++){
								bw.write(Datalist.get(i)+"\r\n");
							}
							for(int i = 0; i < list.size(); i++){
								bw.write(list.get(i)+"\r\n");
							}
						}else{
							for(int i = 0; i < Datalist.size(); i++){
								bw.write(Datalist.get(i)+"\r\n");
							}
						}
						bw.close();
						fw.close();
					}else{
						FileWriter ffw = new FileWriter(Output_File);//每次覆盖以前数据
						BufferedWriter bbw = new BufferedWriter(ffw);
						if( Head != null){
							bbw.write(Head+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2"+"\r\n");// 往文件上写头信息
						}else{
							String FileHead ="#SampleID"+"\t"+"Extension"+"\t"+"Folder"+"\t"+"FileName_1"+"\t"+"FileName_2";
							bbw.write(FileHead+"\r\n");
						}
						if(list.size() != 0){
							for(int i = 0; i < Datalist.size(); i++){
								String str1[] = Datalist.get(i).split("\t");
								for(int j = 0; j < list.size(); j++){
									String str2[] = list.get(j).split("\t");
									if(str1[0].equals(str2[0])){
										bbw.write(Datalist.get(j)+"\t"+str2[str2.length-1]+"\r\n");
									}else{
										continue;
									}
								}
							}
						}else{
							for(int i = 0; i < Datalist.size(); i++){
								bbw.write(Datalist.get(i)+"\r\n");
							}
						}
						bbw.close();
						ffw.close();
					}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	

	//全盘模式
	public void All_Show_Data(String SampleID_Path, String Extension_Data, String SampleID_PorjectName, String Output_Sub_Directory, ArrayList <String> Search_Path, String Date, int loggn, int loog, String Extension ){
		String dir_name = SampleID_Path + "/" + Output_Sub_Directory;
		String Sample_statistics_head = Extension_Data + "_" + Output_Sub_Directory;
		ArrayList <String> day_list = new ArrayList <String>();
		AnchorDx_CollectData_SearchFiles.my_mkdir( dir_name );
		Black_List.clear();
		White_List.clear();
		AnchorDx_CollectData_SearchFiles.ReadLibfile(SampleID_File_list_get, IFILE_List);
		list = AnchorDx_CollectData_SearchFiles.Return_FilePath(IFILE_List, Search_Path, day_list, Extension_Data, Extension);
		Separate_Black_White_List(list, Black_List, White_List);
		Output_File_Black = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_Black.txt";
		Output_File_White = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_White.txt";
		Source_File_Black = "Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_Black.txt";
		Source_File_White = "Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_White.txt";
		Link_File_Black = SampleID_Path  + "/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + "Black.txt";
		Link_File_White = SampleID_Path  + "/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + "White.txt";
		Sample_statistics  = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + "样本统计表_" + Date + "_.txt";
		String Source_Sample_statistics = "Master/" + SampleID_PorjectName + "_" + "样本统计表_" + Date + "_.txt";
		String Link_Sample_statistics = SampleID_Path + "/" + SampleID_PorjectName + "_" + "样本统计表" + ".txt";
		//显示文件查找结果
		if( Extension.equals("R[1-2]_001.fastq.gz") || Extension.equals("R[1-2]_001.clean.fastq.gz") ){
			TwoExtension_write_show( Head, Output_File_Black, Black_List, loggn, Extension);
			TwoExtension_write_show( Head, Output_File_White, White_List, loggn, Extension);
		}else{
			AnchorDx_CollectData_SearchFiles.write_show( Head, Output_File_Black, Black_List, loggn );
			AnchorDx_CollectData_SearchFiles.write_show( Head, Output_File_White, White_List, loggn );
		}
		MyLink(White_List, dir_name);
		Write_Sample_statistics(Sample_statistics_head, Sample_statistics, day_list, loog);
		try{
			String cmd_Black = "ln -s -f " + Source_File_Black + " " + Link_File_Black;
			//System.out.println("pwd");
			Process process_Black = Runtime.getRuntime().exec(cmd_Black);//链接黑名单
			
			String cmd_White = "ln -s -f " + Source_File_White + " " + Link_File_White;
			Process process_White = Runtime.getRuntime().exec(cmd_White);//链接白名单
			
			String cmd_Sample_statistics = "ln -s -f " + Source_Sample_statistics + " " + Link_Sample_statistics;
			Process process_Sample_statistics = Runtime.getRuntime().exec(cmd_Sample_statistics);//链接样本统计表
			//System.out.println(Runtime.getRuntime().exec("pwd"));
			
			//Upload_File(SampleID_PorjectName, Output_File_Black, Output_File_White);//上传文件
			
		}catch(Exception e){
			System.out.println("链接出错！");	
		}
	}
	
	//获取白名单SampleID列表
	public static ArrayList<String>  Read_White_List(ArrayList<String> White_All_Data){
		ArrayList<String> SampleID_Data = new ArrayList<String>();
		for(int i = 0; i < White_All_Data.size(); i++){
			String str[] = White_All_Data.get(i).split("\t");
			if( SampleID_Data.contains(str[0]) ){
				continue;
			}else{
				SampleID_Data.add(str[0]);
			}
		}
		return SampleID_Data;
	}
	
	//获取需要更新的SampleID列表（根据白名单获取）
	public static ArrayList<String>  Updata_White_List(ArrayList<String> All_SampleID_list, ArrayList<String> White_SampleID_list){
		ArrayList<String> Updata_ID_list = new ArrayList <String>();
		for(int i = 0; i < All_SampleID_list.size(); i++){
			if( White_SampleID_list.contains(All_SampleID_list.get(i)) ){
				continue;
			}else{
				Updata_ID_list.add(All_SampleID_list.get(i));
			}
		}
		return Updata_ID_list;
	}
	
	//获取需要更新的SampleID列表（根据黑名单获取
	@SuppressWarnings("unused")
	public static ArrayList<String>  Updata_Black_List(String Updata_File_Path, String Updata_File_part_name, ArrayList<String> Black_SampleID_list){
		ArrayList<String> Updata_ID_list = new ArrayList <String>();
		ArrayList<String> Updata_Flie_list = new ArrayList <String>();
		
		File UFP = new File(Updata_File_Path);
		// 判断目录下是不是空的
		if( UFP == null ) {
			System.out.println("该目录为空："+UFP.getName());
			return null;
		}else{
			for (File pathname : UFP.listFiles())
			{
				if (pathname.isFile()) { //如果是目录
					
					//String Folder = file.getParent();//获取文件的绝对路径			
					//String FileName = file.getName();//文件文件名
					String this_File_name = pathname.getName();
					String Updatafile = pathname.getParent() + "/" + this_File_name;
					
					if( this_File_name.contains(Updata_File_part_name) ){
						AnchorDx_DataOrganizer_2.ReadLibfile(Updatafile, Updata_Flie_list);
					}else{
						continue;	
					}
				}else{
					continue;	
				}
			}
		
			for(int i = 0; i < Black_SampleID_list.size(); i++){
				//Updata_ID_list.add(Black_SampleID_list.get(i));
				if(Updata_ID_list.contains(Black_SampleID_list.get(i))){
					continue;
				}else{
					Updata_ID_list.add(Black_SampleID_list.get(i));
				}
			}
			for(int i = 0; i < Updata_Flie_list.size(); i++){
				//Updata_ID_list.add(Updata_Flie_list.get(i));
				if(Updata_ID_list.contains(Updata_Flie_list.get(i))){
					continue;
				}else{
					Updata_ID_list.add(Updata_Flie_list.get(i));
				}
			}
		}
		return Updata_ID_list;
	}
	
	//更新文件统计表
	public static void Updata_Sample_statistics(String Sample_statistics_head, String Input_File, String Output_File, ArrayList<String> Upday_list ){
		String encoding = "GBK";
		File Infile = new File(Input_File);
		File Outfile = new File(Output_File);
		ArrayList<String> Day_list = new ArrayList<String>();
		ArrayList<String> UD_list = new ArrayList<String>();
		try {
			Day_list.clear();
			UD_list.clear();
			if(Infile.isFile() && Infile.exists()){ //判断文件是否存在
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Infile), encoding));
				String lineTxt = null;
				while((lineTxt = reader.readLine()) != null){
					Day_list.add(lineTxt);
				}
				reader.close();
			}else{
				System.out.println("找不到指定的文件："+Input_File);
				return;
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Outfile), encoding));
			for(int i = 0; i < Upday_list.size(); i++){
				int x = 0;
				int y = 0;
				int log = 0;
				String str[] = Upday_list.get(i).split("\t");
				for(int k = 0; k < Day_list.size(); k++){
					if(k == 0){
						String str0[] = Day_list.get(k).split("\t");
						//String Data = null;
						for(int j = 0; j < str0.length-1; j++){
							if(str0[j].equals(Sample_statistics_head)){
								x = j;
							}else{
								continue;
							}
						}
					}else{
						//writer.write(Day_list.get(i)+ "\t" + list.get(i-1) + "\r\n");
						String str1[] = Day_list.get(k).split("\t");
						String Data = null;
						if(str1[0].equals(str[0])){
							str1[x] = str[1];
							for(int j = 0; j < str1.length; j++){
								if(j == 0){
									Data = str1[j];
								}else{
									Data += "\t" + str1[j];
								}
							}
							UD_list.add(Data);
							log = 1;
							continue;
						}else{
							continue;
						}
					}
				}
				if(log == 0){
					String str_null[] = Day_list.get(0).split("\t");
					//str1[x] = str[1];
					String Data = null;
					for(int j = 0; j < str_null.length-1; j++){
						if(j == 0){
							Data = str[j];
						}else if( j == x){
							Data += "\t"+str[1];
						}else{
							Data += "\t" + "NA";
						}
					}
					//UD_list.add(Data);
					Day_list.add(Data + "\t" + "Reserve");
				}
			}
			for(int i = 0; i < Day_list.size(); i++){
				int logg = 0;
				String strD[] = Day_list.get(i).split("\t");
				if(i == 0){
					writer.write(Day_list.get(i) + "\r\n");	
				}else{
					for(int t = 0; t < UD_list.size(); t++){
						String strU[] = UD_list.get(t).split("\t");
						if(strD[0].equals(strU[0])){
							writer.write(UD_list.get(t) + "\r\n");
							logg = 1;
							continue;
						}else{
							continue;
						}
					}
					if( logg == 0){
						writer.write(Day_list.get(i) + "\r\n");
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//更新模式
	public void Updata_Show_Data(String SampleID_Path, String Extension_Data, String SampleID_PorjectName, String Output_Sub_Directory, ArrayList <String> Search_Path, String Date, int loggn, int loog, String Extension ){
		String dir_name = SampleID_Path + "/" + Output_Sub_Directory;
		String Sample_statistics_head = Extension_Data + "_" + Output_Sub_Directory;
		String Updata_statistics_head = null;
		ArrayList <String> day_list = new ArrayList <String>();
		ArrayList <String> Old_list = new ArrayList <String>();
		//ArrayList <String> White_SampleID_list = new ArrayList <String>();
		ArrayList <String> Black_SampleID_list = new ArrayList <String>();

		AnchorDx_CollectData_SearchFiles.my_mkdir( dir_name );
		Black_List.clear();
		White_List.clear();
		
		//Search_Path.add(SPath.get(i));
		Output_File_Black = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_Black.txt";
		Output_File_White = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_White.txt";
		Source_File_Black = "Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_Black.txt";
		Source_File_White = "Master/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + Date + "_White.txt";
		Link_File_Black = SampleID_Path  + "/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + "Black.txt";
		Link_File_White = SampleID_Path  + "/" + SampleID_PorjectName + "_" + Output_Sub_Directory + "_FileList_" + "White.txt";
		Sample_statistics  = SampleID_Path + "/Master/" + SampleID_PorjectName + "_" + "样本统计表_" + Date + "_.txt";
		String Source_Sample_statistics = "Master/" + SampleID_PorjectName + "_" + "样本统计表_" + Date + "_.txt";
		String Link_Sample_statistics = SampleID_Path + "/" + SampleID_PorjectName + "_" + "样本统计表" + ".txt";
		String Updata_File_Path = SampleID_Path;
		String Updata_File ="样本处理追踪表v1_广州基准医疗_Updata_.txt";
		//Updata_statistics_head = AnchorDx_CollectData_SearchFiles.ReadLibfile(Link_Sample_statistics, Old_list);//读全部样品统计表到列表
		
		if( loggn == 0 ){
			//AnchorDx_CollectData_SearchFiles.ReadLibfile(SampleID_File_list_get, IFILE_List);//读全部SampleID到列表
			//AnchorDx_CollectData_SearchFiles.ReadLibfile(Link_File_White, White_All_Data);
			//White_SampleID_list = Read_White_List(White_All_Data);//获取白名单SampleID列表
			//Updata_SampleID_list = Updata_White_List(IFILE_List, White_SampleID_list);//获取需要更新的SampleID列表
			
			AnchorDx_CollectData_SearchFiles.ReadLibfile(Link_File_White, White_All_Data);
			AnchorDx_CollectData_SearchFiles.ReadLibfile(Link_File_Black, Black_All_Data);
			Black_SampleID_list = Read_White_List(Black_All_Data);//获取黑名单SampleID列表
			Updata_SampleID_list = Updata_Black_List(Updata_File_Path, Updata_File, Black_SampleID_list);
		}
		list = AnchorDx_CollectData_SearchFiles.Return_FilePath(Updata_SampleID_list, Search_Path, day_list, Extension_Data, Extension);
		Separate_Black_White_List(list, Black_List, White_List);
		for(int i = 0; i < White_List.size(); i++){
			//White_All_Data.add(White_List.get(i));
			if( White_All_Data.contains(White_List.get(i)) ){
				continue;
			}else{
				White_All_Data.add(White_List.get(i));
			}
		}
		//显示文件查找结果
		if( Extension.equals("R[1-2]_001.fastq.gz") || Extension.equals("R[1-2]_001.clean.fastq.gz") ){
			TwoExtension_write_show( Head, Output_File_Black, Black_List, loggn, Extension);
			Updata_TwoExtension_write_show( Head, Link_File_White, Output_File_White, White_List, loggn, Extension);
		}else{
			AnchorDx_CollectData_SearchFiles.write_show( Head, Output_File_Black, Black_List, 0 );
			AnchorDx_CollectData_SearchFiles.write_show( Head, Output_File_White, White_All_Data, 0 );
		}
		MyLink(White_List, dir_name);
		
		//Write_Sample_statistics(Sample_statistics_head, Sample_statistics, day_list, loog);
		Updata_Sample_statistics(Sample_statistics_head, Link_Sample_statistics, Sample_statistics, day_list );
		try{
			String cmd_Black = "ln -s -f " + Source_File_Black + " " + Link_File_Black;
			//System.out.println("pwd");
			Process process_Black = Runtime.getRuntime().exec(cmd_Black);//链接黑名单
			
			String cmd_White = "ln -s -f " + Source_File_White + " " + Link_File_White;
			Process process_White = Runtime.getRuntime().exec(cmd_White);//链接白名单
			
			String cmd_Sample_statistics = "ln -s -f " + Source_Sample_statistics + " " + Link_Sample_statistics;
			Process process_Sample_statistics = Runtime.getRuntime().exec(cmd_Sample_statistics);//链接样本统计表
			//System.out.println(Runtime.getRuntime().exec("pwd"));
			
			//Upload_File(SampleID_PorjectName, Output_File_Black, Output_File_White);//上传文件
			
		}catch(Exception e){
			System.out.println("链接出错！");	
		}
	}
	
	//分离黑白名单列表
	public static void Separate_Black_White_List(ArrayList <String> Source_List, ArrayList <String> Black_List, ArrayList <String> White_List){
		for(int i = 0; i < Source_List.size(); i++){
			String str[] = Source_List.get(i).split("\t");
			if( str[2].equals("NA") ){
				Black_List.add(Source_List.get(i));
			}else{
				White_List.add(Source_List.get(i));
			}
		}
	}
	
	//做软连接
	public static void MyLink(ArrayList <String> InputList, String Link_Path){
		try{
			String Source_File = null;
			for(int i = 0; i < InputList.size(); i++){
				String str[] = InputList.get(i).split("\t");
				if( !(str[2].equals("NA")) ){
					Source_File = str[2]+"/"+str[3];
					String cmd = "ln -s -f " + Source_File + " " + Link_Path + "/" + str[3];
					Process process = Runtime.getRuntime().exec(cmd);
					/*BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = "";
					while ((line = input.readLine()) != null) {
						System.out.println(line);
					}*/
				}
			}
		}catch(Exception e){
			System.out.println("链接出错！");
		}
	}
	
	//显示结果
	public void Show_Data(String SampleID_Path, String Extension_Data, String SampleID_PorjectName, String Output_Sub_Directory, ArrayList <String> Search_Path, String Date, int loggn, int loog, String Extension, int Pattern )
	{
		if(Pattern == 0){
			this.All_Show_Data(SampleID_Path, Extension_Data, SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, loggn, loog, Extension );//全盘模式
		}else{
			this.Updata_Show_Data(SampleID_Path, Extension_Data, SampleID_PorjectName, Output_Sub_Directory, Search_Path, Date, loggn, loog, Extension );//更新模式
		}
	}
}
