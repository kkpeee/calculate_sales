package jp.co.iccom.higashi_takahiro.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Calculatesales {
		public static void main(String[]args) throws IOException{
			
			
			
			
			if(args.length == 0 || args.length > 1  || !new File(args[0]).exists()){
				System.out.println("er");
				return;
			}
			
			//支店定義ファイルMap
			HashMap<String, String> branchMap = new HashMap<String, String>();
			//商品定義ファイルMap
			HashMap<String, String> commodityMap = new HashMap<String, String>();
			//売り上げMap(支店コード・商品コードをkey、金額をvalueに格納)
			HashMap<String, Long> rcdBranchMap = new HashMap<String, Long>();
			HashMap<String, Long> rcdCommodity = new HashMap<String, Long>();
			
			//支店定義ファイルの読み込み、フォーマットチェック、存在判定
			try {
	            File branch = new File(args[0] + File.separator + "branch.lst");
	            FileReader frBranch = new FileReader(branch);
	            BufferedReader br = new BufferedReader(frBranch);
	            
	            String line;
	            
	            while ((line = br.readLine()) != null) {
	            	String[] words =line.split(",");
	                //System.out.println(words[1]);
	               try {
	            	   if (words.length != 2 || !words[0].matches("^\\d{3}$")|| words[1].matches( "[a-zA-Z_0-9]*$")) {
	            		   System.out.println("定義ファイルのフォーマットが不正です");
					}
	            	   for(int i = 0; i < words.length; i++){
							branchMap.put(words[0],words[1]);
	            	   }
				} catch (Exception e) {
					System.out.println("予期せぬエラーが発生しました");
					frBranch.close();
				}
	            }
	            
	            frBranch.close();
	            
	        } catch (IOException e) {
	            System.out.println("支店定義ファイルが見つかりません。");
	        }
		
			
			//商品定義ファイルの読み込み
			try {
	            File commodity = new File(args[0], "commodity.lst");
	            BufferedReader br = new BufferedReader(new FileReader(commodity));
	            
	            String line;
	            
	            while ((line = br.readLine()) != null) {
	            	String[] words =line.split(",");
	                //System.out.println(words[1]);
	               try {
	            	   if (words.length != 2 || !words[0].matches( "^\\w{8}$")|| words[1].matches( "[0-9]*$")) {
	            		   System.out.println("定義ファイルのフォーマットが不正です");
					}
	            	   for(int i = 0; i < words.length; i++){
	            		   commodityMap.put(words[0],words[1]);
	            	   }
				} catch (Exception e) {
					System.out.println("予期せぬエラーが発生しました");
				
					
				}finally {
					br.close();
				}
	           }
	            
	            
	        } catch (IOException e) {
	            System.out.println("支店定義ファイルが見つかりません。");
	        }
			
			// ディレクトリのファイル一覧を取得
			String path = args[0];
			File folder = new File(path);
			File[] folderList = folder.listFiles(); // dir内のファイルを配列に格納

			// 売上ファイル格納
			ArrayList<String> rcdFolder = new ArrayList<String>();
			ArrayList<Integer> rcdNo = new ArrayList<Integer>();

			// 売上ファイル抽出
			for (File value : folderList) { // 
				File inputFile = value;
				if (inputFile.getName().matches("^\\d{8}.rcd$")) { // 数字8桁、rcdファイル検索
					rcdFolder.add(inputFile.getName());
					String[] rcdSplit = inputFile.getName().split("\\.");
					rcdNo.add( new Integer(rcdSplit[0]).intValue());
				}
			}

//			System.out.println(rcdNo);
					
			// 連番処理
			for (int i = 0; i < rcdNo.size(); i++) {
				if(rcdNo.get(i) != i + 1){
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}
			}
			
			//売り上げファイル読み込み
			
			BufferedReader br = null;
			for(int i = 0; i < rcdFolder.size(); i++){
				try{
					br = new BufferedReader(new FileReader(new File(folder, rcdFolder.get(i))));
					String readLine;
					ArrayList<String> rcdEarings = new ArrayList<String>();
					while((readLine = br.readLine()) != null){
						rcdEarings.add(readLine); //get(0)001,get(1)SFT0001,get(2)10000
					}
					//売り上げファイルの中身が４行以上ある場合フォーマットエラー
					if((rcdEarings.size() != 3)) {
						System.out.println(rcdFolder.get(i) + "のフォーマットが不正です");
						return;				
				}
					//支店別
					if (!branchMap.containsKey(rcdEarings.get(0))) {
						System.out.println(rcdFolder.get(i) + "の支店コードが不正です");
						return;
					}
				
					
					//商品別
					if (!commodityMap.containsKey(rcdEarings.get(0))) {
						System.out.println(rcdFolder.get(i) + "の支店コードが不正です");
						return;
					}
				}catch(Exception e){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				finally{
					if(br != null){
						br.close();
					}
				}
			}
			return;

					
			
				
			
					
			
	    }
		
}	