package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report; //models.Report内のクラス名や型名を省略表記するためにインポートする

public class ReportValidator {
	
	public static List<String> validate(Report r) { 
		List<String> errors = new ArrayList<String>(); //リストerrorsを生成
		
		String title_error = _validateTitle(r.getTitle());   //文字列title_errorに_validateTitleﾒｿｯﾄﾞ（下部記載）の実行結果を代入
		
		if(!title_error.equals("")) {                             //もしtitle_errorが存在したら
			errors.add(title_error);
		}
		
		String content_error = _validateContent(r.getContent()); //文字列title_errorに_validateContentﾒｿｯﾄﾞ（下部記載）の実行結果を代入
		
		if(!content_error.equals("")) {                       //もしcontent_errorが存在したら
			errors.add(content_error);
		}
		
		return errors;
	}
	
	private static String _validateTitle(String title) {
		if(title == null || title.equals("")) {                  //もしtitleがnull もしくは titleが無だったら
			return "タイトルを入力してください。";
		}
		
		return "";
	}
	
	private static String _validateContent(String content) {
		if(content == null || content.equals("")) {       //もしcontentがnull もしくは contentが無だったら
			return "内容を入力してください。";
		}
		
		return "";
	}
}

