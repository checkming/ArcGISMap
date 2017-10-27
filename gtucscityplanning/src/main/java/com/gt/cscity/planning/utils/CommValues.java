package com.gt.cscity.planning.utils;

import android.content.Context;
import android.widget.Toast;

import com.gt.cscity.planning.ui.activity.LoginActivity;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


// 公共变量
public class CommValues {

	public static String sdCardDir="";
	public static String xmlPath="";
	public static String streetUrl = "";
	public static String imagPath="";
	public static String dbPath ="";
	public static String filePath ="";
	public static String upload="";
	public static String stylesPath="";
	public static String publicsPath="";
	public static String ClientID="";
	public static String restUrl="";
	public static String bsUrl="";
	public static String interfaceUrl="";


	public static boolean Inite(Context  context)
	{
		// *****只有此处可固定写死，其他地方均不允许固定写死*****//
//				dbPath = context.getFilesDir().getAbsolutePath()+"/databases/cspocketPlan.db";
		String relativesdCardDir="/CSGHPocketPlan";
		String pocketDir="/csghPocketPlan.xml";
		CommonHelper comhelper=new  CommonHelper();
		xmlPath=comhelper.getSDcardPath("/0"+relativesdCardDir+pocketDir);
//				xmlPath = context.getFilesDir().getAbsolutePath()+"/xmlit/csghpocketplan.xml";
		File file = new File(xmlPath);
		if (file.exists()==false)
		{
			sdCardDir=comhelper.getSDcardPath("/1"+relativesdCardDir+pocketDir);
			file = new File(sdCardDir);
			if (file.exists()==false)
			{
				Toast.makeText(context, "系统参数配置错误,请联系管理员!",Toast.LENGTH_LONG).show();
				return false;
			}
		}

				/*filePath = comhelper.getSDcardPath("/0/CSGHPocketPlan/files");
				File file_doc = new File(filePath);
				if(!file_doc.exists()){
					filePath = comhelper.getSDcardPath("/1/CSGHPocketPlan/files");
					file = new File(filePath);
					if(!file.exists()){
						Toast.makeText(context, "资源文件不存在!",Toast.LENGTH_LONG).show();
					}
				}*/

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file.toURI().toString());
			NodeList restUrl_Node = doc.getElementsByTagName("restUrl");
			restUrl=restUrl_Node.item(0).getTextContent().toString();
			NodeList bsUrl_Node = doc.getElementsByTagName("bsUrl");
//			bsUrl=bsUrl_Node.item(0).getTextContent().toString();
			bsUrl= LoginActivity.sp.getString("fwqurl", "");
			interfaceUrl = bsUrl+"/service.do?";
			NodeList szstreet = doc.getElementsByTagName("szstreet");
			streetUrl=szstreet.item(0).getTextContent().toString();
			// 图片地址
			NodeList imgdirnode = doc.getElementsByTagName("imagepath");
			imagPath=imgdirnode.item(0).getTextContent().toString();
			imagPath=comhelper.getSDcardPath(imagPath);
			NodeList dbpathnode = doc.getElementsByTagName("dbpath");
			dbPath=dbpathnode.item(0).getTextContent().toString();
			dbPath=comhelper.getSDcardPath(dbPath);
			File dbFile = new File(dbPath);
			if(!dbFile.exists()){
				dbPath = context.getFilesDir().getAbsolutePath()+"/databases/cspocketPlan.db";
			}
			NodeList filepathnode = doc.getElementsByTagName("filepath");
			/*filePath=filepathnode.item(0).getTextContent().toString();
			filePath=comhelper.getSDcardPath(filePath);*/
			NodeList uploadnode = doc.getElementsByTagName("upload");
			upload=uploadnode.item(0).getTextContent().toString();
			upload=comhelper.getSDcardPath(upload);
			NodeList stylesnode = doc.getElementsByTagName("styles");
			stylesPath=stylesnode.item(0).getTextContent().toString();
			stylesPath=comhelper.getSDcardPath(stylesPath);
			NodeList publicsnode = doc.getElementsByTagName("publics");
			publicsPath=publicsnode.item(0).getTextContent().toString();
			NodeList clientIDnode = doc.getElementsByTagName("ClientID");
			ClientID=clientIDnode.item(0).getTextContent().toString();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
