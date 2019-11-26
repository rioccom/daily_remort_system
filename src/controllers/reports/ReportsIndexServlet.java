package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;


@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	public ReportsIndexServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = DBUtil.createEntityManager(); //--------------------//★EntityManager生成
	
		int page;                                                                                                     //数：pageを宣言
		
		try{
			page = Integer.parseInt(request.getParameter("page"));                                     //リクエストスコープからパラメータpageをgetしてint化してpageに代入
		} catch(Exception e) {                                                                                    //例外が起きた場合は
			page = 1;                                                                                                //pageに1を代入
		}
		
		List<Report> reports = em.createNamedQuery("getAllReports", Report.class)             //emでクエリgetAllReportsを走らせて結果をリストreportsに代入
			.setFirstResult(15 * (page - 1)) //開始位置で指定したクエリ結果だけを取得する
			.setMaxResults(15)                 //複数件あるクエリ結果の中から任意に指定した件数だけを取得する
			.getResultList();                      //クエリの実行および結果の取得はgetSingleResultまたはgetResultListで行う。
		
		long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)
			.getSingleResult();  //↑※通常はint型で十分ですが極めて大きい数値を扱う場合にはlong型を使います。
		
		em.close();  //---------------------------------------------------//★EntityManager破棄
		
		request.setAttribute("reports", reports);                      //リクエストスコープのreports属性にreportsをset
		request.setAttribute("reports_count", reports_count);     //      〃     reports_count属性にreports_countをset
		request.setAttribute("page", page);                             //     〃                  page属性にpageをset
		
		if(request.getSession().getAttribute("flush") != null) {                               //もしセッションスコープ内の属性flushが存在すれば
			request.setAttribute("flush", request.getSession().getAttribute("flush")); //リクエストスコープのflush属性にそれを代入
			request.getSession().removeAttribute("flush");                                  //セッションスコープのflush属性は消しておく
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp"); //index.jspにフォワード
		rd.forward(request, response);
	}

}