package jp.co.iccom.higashi_takahiro.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Calculatesales {
		public static void main(String[]args){
			
			if(args.length == 0){
				System.out.println("コマンドライン引数を入れてください");
				return;
			}else if(args.length > 1){
				System.out.println("コマンドライン引数は１つにしてください");
				return;
			}
			
			//支店定義ファイルMap
			HashMap<String, String> branchMap = new HashMap<String, String>();
			//商品定義ファイルMap
			HashMap<String, String> commodityMap = new HashMap<String, String>();
			//売り上げMap(支店コード・商品コードをkey、金額をvalueに格納)
			HashMap<String, Long> rcdBranch = new HashMap<String, Long>();
			HashMap<String, Long> rcdCommodity = new HashMap<String, Long>();
			
			//支店定義ファイルの読み込み
			try {
	            File branch = new File(args[0] + File.separator + "branch.lst");
	            FileReader in = new FileReader(branch);
	            BufferedReader br = new BufferedReader(in);
	            
	            String line;
	            
	            while ((line = br.readLine()) != null) {
	            	//map
	                System.out.println(line);
	            }
	            
	            in.close();
	            
	        } catch (IOException e) {
	            System.out.println("支店定義ファイルが見つかりません。");
	        }
			
			//商品定義ファイルの読み込み
			try {
	            File commodity = new File(args[0], "commodity.lst");
	            FileReader in = new FileReader(commodity);
	            BufferedReader br = new BufferedReader(in);
	            
	            String line;
	            
	            while ((line = br.readLine()) != null) {
	                System.out.println(line);
	            }
	            
	            in.close();
	            
	        } catch (IOException e) {
	            System.out.println("支店定義ファイルが見つかりません。");
	        }
	    }
		
}		

		
