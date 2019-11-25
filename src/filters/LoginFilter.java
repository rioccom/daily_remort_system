package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

@WebFilter("/*") //フィルタなので「/*」
public class LoginFilter implements Filter {
	
	public LoginFilter() {
	}

	public void destroy() {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String context_path = ((HttpServletRequest)request).getContextPath(); //文字列context_pathにコンテキストパスを代入
		String servlet_path = ((HttpServletRequest)request).getServletPath();   //文字列servlet_pathにサーブレットパスを代入
		
		
		//                                                                                        //【分岐始まり】_________________________________________________________
		if(!servlet_path.matches("/css.*")) {                                              //■もしcssのファイルならば
			HttpSession session = ((HttpServletRequest)request).getSession();          //セッションをｇｅｔして変数sessionに代入
			
			// セッションスコープに保存された従業員（ログインユーザ）情報を取得
			Employee e = (Employee)session.getAttribute("login_employee");               //変数sessionの属性login_employeeをgetしてeに代入
			
			if(!servlet_path.equals("/login")) {        // ログイン画面以外について            //├■もしサーブレットパスが/loginでない→
				// ログアウトしている状態であれば
				// ログイン画面にリダイレクト
				if(e == null) {                                                                                      //├→■且つeがnullならば＝未ログインであれば
					((HttpServletResponse)response).sendRedirect(context_path + "/login");                 // /loginにリダイレクトする
					return;
				}
				
				// 従業員管理の機能は管理者のみが閲覧できるようにする
				if(servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {                //└→■且つサーブレットパスが/employeesで、且つ管理者でないなら
					((HttpServletResponse)response).sendRedirect(context_path + "/");                        //システムのトップページにリダイレクト
					return;
				}
				
			} else {                                    // ログイン画面について                       //└■サーブレットパスが/loginであれば→
				// ログインしているのにログイン画面を表示させようとした場合は
				// システムのトップページにリダイレクト
				if(e != null) {                                                                                        //└■且つeがnullでなければ
					((HttpServletResponse)response).sendRedirect(context_path + "/");                        //システムのトップページにリダイレクト
					return;
				}
				
			}
			
		}                                                                                           //【分岐終わり】^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		
		chain.doFilter(request, response);                        //FileterChain オブジェクト(chain.doFilter()) を利用してチェーンの次のエンティティを呼び出す
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}
	
}

