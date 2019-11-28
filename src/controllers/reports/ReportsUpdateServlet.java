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

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;


@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public ReportsUpdateServlet() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String _token = (String)request.getParameter("_token");                               //ﾊﾟﾗﾒｰﾀ_tokenを取得してString化し、文字列Stringに代入
		
		if(_token != null && _token.equals(request.getSession().getId())) {                    //_tokenがちゃんと存在して且つセッションIDと同一であれば
			
			EntityManager em = DBUtil.createEntityManager();                 //★EntityManager生成
			
			Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id"))); //セッション属性report_idを取得してInt化したものと、同一のidを持つレコードを、Report.classの中で１件探し出す。
			
			r.setReport_date(Date.valueOf(request.getParameter("report_date")));         //ﾊﾟﾗﾒｰﾀreport_dateをミリ秒形式化して、r のreport_dateにセットする。
			r.setTitle(request.getParameter("title"));                                              //ﾊﾟﾗﾒｰﾀtitleを、r のTitleにセットする。
			r.setContent(request.getParameter("content"));                                    //ﾊﾟﾗﾒｰﾀcontentを、r のContentにセットする。
			r.setUpdated_at(new Timestamp(System.currentTimeMillis()));                   //現在時刻を引数としてｵﾌﾞｼﾞｪｸﾄTimestampを生成し、r のUpdated_atにセットする。
			
			List<String> errors = ReportValidator.validate(r);                                    //r をvalidateして、ﾘｽﾄerrors に代入する。
			
			if(errors.size() > 0) {                                                                       //■もしｴﾗｰが存在したら、
				em.close();                                                             //★EntityManager破棄
				
				request.setAttribute("_token", request.getSession().getId());                                //└→属性_tokenにセッションIdを代入する。
				request.setAttribute("report", r);                                                                   //      属性reportにr を代入する。
				request.setAttribute("errors", errors);                                                            //      属性errorsにerorrsを代入する。
				
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
				rd.forward(request, response);                                                                      //     edit.jspにフォワードする //終//
				
			} else {
				em.getTransaction().begin();                                                                         //Transaction開始
				em.getTransaction().commit();                                                                      //コミット
				em.close();                                                             //★EntityManager破棄
				
				request.getSession().setAttribute("flush", "更新が完了しました。");                        //セッション属性flushに文言を登録
				
				request.getSession().removeAttribute("report_id");                                            //セッション属性report_idを削除
				
				response.sendRedirect(request.getContextPath() + "/reports/index");                   //indexにリダイレクト
				
			}
			
		}
		
	}
	
}