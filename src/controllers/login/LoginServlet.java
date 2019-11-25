package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	public LoginServlet() {
		super();
	}
	
	//____ログイン画面を表示_______________________________________________
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("_token", request.getSession().getId());//属性_tokenにIdをセット
		request.setAttribute("hasError", false);                          //属性hasErrorにfalseをセット
		if(request.getSession().getAttribute("flush") != null) {        //もし属性flushがnullならば
			request.setAttribute("flush", request.getSession().getAttribute("flush"));//セッションスコープからflushをgetしてセットする
			request.getSession().removeAttribute("flush");                                 //そしてgetSessionしたものをremoveする
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp"); //login.jspにフォワード
		rd.forward(request, response);
	}
	
	
	//____ログイン処理を実行_______________________________________________
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Boolean check_result = false;// 認証結果を格納する変数
		
		String code = request.getParameter("code");              //文字列codeにcodeのパラメータを代入
		String plain_pass = request.getParameter("password"); //文字列plain_passにpasswordのパラメータを代入
		
		Employee e = null;                                                //Employee型ﾃﾞｰタeにnullを代入
	
		if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) { //セキュリティチェックOKなら
			EntityManager em = DBUtil.createEntityManager(); //★EntityManager生成
			
			String password = EncryptUtil.getPasswordEncrypt( //パスワード暗号化テンプレ
				plain_pass,
				(String)this.getServletContext().getAttribute("salt")
				);
				
			try {                                                             // 社員番号とパスワードが正しいかチェックする
			e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class) //eにクエリcheckLoginCodeAndPasswordを実行？
				.setParameter("code", code)                        //各種パラメータをset
				.setParameter("pass", password)
				.getSingleResult();
			} catch(NoResultException ex) {}                        //？
			
			em.close();                                                      //★EntityManager破棄
			
			if(e != null) {                                                  //もしeがnullでなければ
			check_result = true;                                         //認証結果を格納する変数check_resultにtrueを代入
			}
		}
	
		if(!check_result) { //■もしcheck_resultがfalseならば（ログイン認証NGならば）…
			request.setAttribute("_token", request.getSession().getId()); //リクエストのIdをgetして_tokenにset
			request.setAttribute("hasError", true); 
			request.setAttribute("code", code);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
			rd.forward(request, response);                                       //login.jspにフォワード
			
		} else {              //■trueであれば…
			request.getSession().setAttribute("login_employee", e);       //リクエストをgetセッションしたもののlogin_employeeにeをｓｅｔ
			
			request.getSession().setAttribute("flush", "ログインしました。");
			response.sendRedirect(request.getContextPath() + "/");     //リダイレクト
		}
		
	}
	
}

