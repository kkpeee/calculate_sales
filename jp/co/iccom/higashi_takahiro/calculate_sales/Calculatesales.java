package jp.co.iccom.higashi_takahiro.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Calculatesales {
	public static void main(String[]args) throws IOException{
			
			
			
			
	if(args.length == 0 || args.length > 1  || !new File(args[0]).exists()){
		System.out.println("予期せぬエラーが発生しました");
		return;
		}
			
	// 支店定義ファイルMap
	HashMap<String, String> branchMap = new HashMap<String, String>();
	// 商品定義ファイルMap
	HashMap<String, String> commodityMap = new HashMap<String, String>();
	// 売り上げMap(支店コード・商品コードをkey、金額をvalueに格納)
	HashMap<String, Long> rcdBranchMap = new HashMap<String, Long>();
	HashMap<String, Long> rcdCommodityMap = new HashMap<String, Long>();
			
	
	 // 支店定義ファイルの読み込み、フォーマットチェック、存在判定
	try {
		File branch = new File(args[0] + File.separator + "branch.lst");
		FileReader frBranch = new FileReader(branch);
		BufferedReader br = new BufferedReader(frBranch);
		
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] words =line.split(",");
			
			try {
				
				if (words.length != 2 || !words[0].matches("^\\d{3}$")|| words[1].matches( "[a-zA-Z_0-9]*$")) {
					System.out.println("定義ファイルのフォーマットが不正です");
					}
				
				for(int i = 0; i < words.length; i++){
					branchMap.put(words[0],words[1]);// [0]支店コード　[1]支店名
					rcdBranchMap.put(words[0],0L);
					}
				
			} catch (Exception e) {
				System.out.println("予期せぬエラーが発生しました");
				frBranch.close();
				}
			}
		frBranch.close();
		} catch (IOException e) {
			System.out.println("支店定義ファイルが存在しません");
			} finally {
				
			}
		
			
	// 商品定義ファイルの読み込み、フォーマットチェック、存在判定
	try {
		File commodity = new File(args[0] + File.separator + "commodity.lst");
		FileReader frCommodity = new FileReader(commodity);
		BufferedReader br = new BufferedReader(frCommodity);
		
		String line;
		
		while ((line = br.readLine()) != null) {
			String[] words =line.split(",");
			
			try {
				if (words.length != 2 || !words[0].matches( "^\\w{8}$")|| words[1].matches( "[0-9]*$")) {
					System.out.println("定義ファイルのフォーマットが不正です");
					}
				
				for(int i = 0; i < words.length; i++){
					commodityMap.put(words[0],words[1]);
					rcdCommodityMap.put(words[0],0L);
				}
			} catch (Exception e) {
				System.out.println("予期せぬエラーが発生しました");
				frCommodity.close(); 
			}
			
		}
		} catch (IOException e) {
			System.out.println("商品定義ファイルが存在しません");
			System.out.println(e);
			} finally {	
				
			}
			
	// ディレクトリのファイル一覧を取得
	String path = args[0];
	File folder = new File(path);
	File[] folderList = folder.listFiles(); // dir内のファイルを配列に格納

	// 売上ファイル格納
	ArrayList<String> rcdFolder = new ArrayList<String>();
	ArrayList<Integer> rcdNo = new ArrayList<Integer>();

	// 売上ファイル抽出
	for (File value : folderList) { 
		File inputFile = value;
		if (inputFile.getName().matches("^\\d{8}.rcd$")) { // 数字8桁、rcdファイル検索
			if(inputFile.isFile()){
				rcdFolder.add(inputFile.getName());
				}else{
					System.out.println("rcdフォルダが存在します、削除してください。");	
					}
			//フォルダを除けって処理
			String[] rcdSplit = inputFile.getName().split("\\.");
			rcdNo.add( new Integer(rcdSplit[0]).intValue());		
			}
		}
					
	// 連番処理
	for (int i = 0; i < rcdNo.size(); i++) {
		if(rcdNo.get(i) != i + 1){
			System.out.println("売上ファイル名が連番になっていません");
			return;
			}
		}	
			
	// 売り上げファイル読み込み
	BufferedReader br = null;
	for(int i = 0; i < rcdFolder.size(); i++){
		try{
			br = new BufferedReader(new FileReader(new File(folder, rcdFolder.get(i))));
			String readLine;
			ArrayList<String> rcdEarings = new ArrayList<String>();
			while((readLine = br.readLine()) != null){
				rcdEarings.add(readLine); //get(0)001,get(1)SFT0001,get(2)10000
				}	
																	
	// 売り上げファイルの中身が４行以上ある場合フォーマットエラー
			if((rcdEarings.size() != 3)) {
				System.out.println(rcdFolder.get(i) + "のフォーマットが不正です");
				return;
				}
													

	// 支店別コードエラー処理
			if (!rcdBranchMap.containsKey(rcdEarings.get(0))) {
				System.out.println(rcdFolder.get(i) + "の支店コードが不正です");
				return ;
				}
				


	// 商品別コードエラー処理
			if (!rcdCommodityMap.containsKey(rcdEarings.get(1))) {
//				System.out.println(rcdEarings.get(1));
				System.out.println(rcdFolder.get(i) + "の商品コードが不正です");
				return;
				}
					
   // 支店別集計
			if(rcdBranchMap.get(rcdEarings.get(0)) != null){
				long branchMount = rcdBranchMap.get(rcdEarings.get(0));
				long branchNewMount = branchMount + Long.parseLong(rcdEarings.get(2));
				rcdBranchMap.put(rcdEarings.get(0),branchNewMount);
//                           System.out.println(rcdFolder.get(i) +"の合計金額は"+branchNewMount+"です");
				if(String.valueOf(branchNewMount).length() > 10){
					System.out.println("合計金額が10桁を超えました");
					}
					
   // 商品別の売上合計
				if(rcdCommodityMap.get(rcdEarings.get(1)) != null){

   // 商品別集計
					long commodityMount = rcdCommodityMap.get(rcdEarings.get(1));
					long commodityNewMount = commodityMount + Long.parseLong(rcdEarings.get(2));
					rcdCommodityMap.put(rcdEarings.get(1),commodityNewMount);
//                           System.out.println(rcdFolder.get(i) +"の合計金額は"+commodityNewMount+"です");
					if(String.valueOf(commodityNewMount).length() > 10){
						System.out.println("合計金額が10桁を超えました");
						}
					}	
}
			}catch(Exception e){
				System.out.println("予期せぬエラーが発生しました");
				return;
				}finally{
					if(br != null){
						br.close();
							}
						}
				}

	// 支店別集計ファイル作成
	BufferedWriter bw = null;
	try{
		File branchout = new File(args[0] + File.separator + "branch.out");
		bw = new BufferedWriter(new FileWriter(branchout));
		branchout.createNewFile();
                    
		//ソート降順
		List<Map.Entry<String, Long>> sortBranch = new ArrayList<Map.Entry<String,Long>>(rcdBranchMap.entrySet());
		Collections.sort(sortBranch, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
						 }
			});
		for (Entry<String, Long> e : sortBranch) {
			bw.write(e.getKey() + "," +branchMap.get(e.getKey()) +","+ e.getValue());
			bw.newLine();
			}
		} catch (IOException e)  {
			System.out.println("予期せぬエラーが発生しました");
			return;
			} finally {
				bw.close();	
				}

	// 商品別集計ファイル作成
	BufferedWriter bw1 = null;
	try{
		File commodityout = new File(args[0] + File.separator + "commodity.out");
		bw1 = new BufferedWriter(new FileWriter(commodityout));
		commodityout.createNewFile();
		//ソート降順
		List<Map.Entry<String, Long>> sortCommodity = new ArrayList<Map.Entry<String,Long>>(rcdCommodityMap.entrySet());
		Collections.sort(sortCommodity, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
									 }
			});
		for (Entry<String, Long> e : sortCommodity) {
			bw1.write(e.getKey() + "," +commodityMap.get(e.getKey()) +","+ e.getValue());
			bw1.newLine();
			}
		} catch (IOException e)  {
			System.out.println("予期せぬエラーが発生しました");
			return;
			} finally {
				bw1.close();	
				}
	return;
	}
		
}	