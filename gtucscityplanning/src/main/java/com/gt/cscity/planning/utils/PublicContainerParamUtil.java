package com.gt.cscity.planning.utils;

import com.esri.android.map.Layer;
import com.esri.core.map.Graphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class PublicContainerParamUtil {
	public static HashMap<String, Graphic[]> search_resluts = null;
	public static ArrayList<String> groupcontent = null;
	public static ArrayList<Layer> showLayerList = null;

	//í3??í?êy?Y
	public static int index1qj = 1;
	public static boolean kaiguan = false;
	public static HashMap<String, LinkedList<Double>> chartDatas = new HashMap<String, LinkedList<Double>>();
	public static ArrayList<String> nameList = new ArrayList<String>();
	public static ArrayList<String> xVals = new ArrayList<String>();
	public static ArrayList<Double> maxNum =new ArrayList<Double>();
	public static ArrayList<Double> axisSteps =new ArrayList<Double>();


	public static HashMap<String, LinkedList<Double>> chartDatas2 = new HashMap<String, LinkedList<Double>>();
	public static ArrayList<String> nameList2 = new ArrayList<String>();
	public static ArrayList<String> xVals2 = new ArrayList<String>();
	public static ArrayList<Double> maxNum2 =new ArrayList<Double>();
	public static ArrayList<Double> axisSteps2 =new ArrayList<Double>();



	public static float updown ;
	public static float updown2 ;
	public static float updownz ;
	public static float updownz2;


	public static ArrayList<String[]> listDataHeader = null;
	public static HashMap<String, List<String[]>> listDataChild = null;
	public static HashMap<String, List<Integer>> listpivc = null;
	public static HashMap<String, Integer> parameter = null;
	public static HashMap<String, Integer> types = null;
	public static String BZDATATYPE = "BZCG";
	public static String SPDATATYPE = "SPCG";
	public static String CXDATATYPE = "ZHCX";
	public static String TJDATATYPE = "ZHTJ";
	public static String FLDATATYPE = "FLFG";
	public static String SZDATATYPE = "SZGL";
	public static String TCDATATYPE = "XTTC";

}
