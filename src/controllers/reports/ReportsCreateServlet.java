package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;


@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public ReportsCreateServlet() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String _token = (String)request.getParameter("_token");            //パラメータ_tokenの中身をｇｅｔしてString化し、文字列_tokenに代入する。
		
		if(_token != null && _token.equals(request.getSession().getId())) {  //■もし_tokenが存在して 且つ セッションスコープの Idと等しければ
			
			EntityManager em = DBUtil.createEntityManager();                                //★EntityManagerを生成
			
			Report r = new Report();                                                                //Report型オブジェクトのr を生成
			
			r.setEmployee((Employee)request.getSession().getAttribute("login_employee")); //セッションスコープの属性login_employeeを取得し、r のｾｯｯﾀｰを通してEmployeeを通してset
			
			Date report_date = new Date(System.currentTimeMillis());                     //Date型オブジェクトのreport_dateを生成
			String rd_str = request.getParameter("report_date");                              //リクエストスコープのパラメータreport_dateを取得して文字列rd_str に代入
			
			if(rd_str != null && !rd_str.equals("")) {                                                //■もしrd_strがnullであり内容が未記入ならば
				report_date = Date.valueOf(request.getParameter("report_date"));                       //→パラメータreport_dateをミリ秒形式に変換してreport_dateに代入 [終]
			}
			
			r.setReport_date(report_date);                                                            //r のセッターを通してreport_dateをセット
			
			r.setTitle(request.getParameter("title"));                                              //パラメータtitleをgetし、r のセッターを通してセット。
			r.setContent(request.getParameter("content"));                                    //パラメータcontentをgetし、r のセッターを通してセット。
			
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());    //Timestamp型オブジェクトcurrentTimeを生成。
			r.setCreated_at(currentTime);                                                            //r のｾｯﾀｰ使用。
			r.setUpdated_at(currentTime);                                                            //r のｾｯﾀｰ使用。
			
			List<String> errors = ReportValidator.validate(r);                                    //r をvalidateして、リスト型オブジェクトerrorsに代入。
			
			if(errors.size() > 0) {                                                                       //■→もしエラーがあれば
				em.close();                                                                                               //EntityManagerを破棄
				
				request.setAttribute("_token", request.getSession().getId());                                 //セッションIdをｇｅｔして属性_tokenに代入？
				request.setAttribute("report", r);                                                                    //属性reportにrを代入
				request.setAttribute("errors", errors);                                                             //属性errorsにerorrsを代入
				
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
				rd.forward(request, response);                                                                       //new.jspにフォワード [終]
				
			} else {                                                                                       //■→エラーがなければ   
				em.getTransaction().begin();                                                                         //「begin」メソッドを実行してトランザクション処理を開始
				em.persist(r);                                                                                            //「persist」メソッドでエンティティを新規追加（保存）
				em.getTransaction().commit();
				em.close();                                                                                               // * 「commit」メソッドを呼び出してコミットすると、
				request.getSession().setAttribute("flush", "登録が完了しました。");                         // * persistしておいたエンティティが全てデータベースに保存されます。
//                                                                                                                                  // * コミットすると同時にトランザクション処理は終了し、DBは開放されます。
				response.sendRedirect(request.getContextPath() + "/reports/index");                    //reports/indexにリダイレクト
			}
			
		}
		
	}
	
}