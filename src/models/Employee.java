//DTO用クラス
package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
@Table(name = "employees") //Entityクラスと対応するデータベースのテーブル名を指定する
@NamedQueries({                 //Entityクラスに名前付きクエリーを定義
	@NamedQuery(
		name = "getAllEmployees", //ﾃｰﾌﾞﾙEmployeeを全てselect, IDが大きい順にソートし→カラム名eとする
		query = "SELECT e FROM Employee AS e ORDER BY e.id DESC" 
		),
	@NamedQuery(
		name = "getEmployeesCount", //ﾃｰﾌﾞﾙEmployeeの全てのレコード件数をカウントし→カラム名eとする
		query = "SELECT COUNT(e) FROM Employee AS e"  
		),
	@NamedQuery(                              //▼指定された社員番号が既存かどうか調べる
		name = "checkRegisteredCode", //ﾃｰﾌﾞﾙEmployeeにおいて"e.code = :code"が成立するレコード件数をカウントし→カラム名eとする
		query = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :code"
		),
	@NamedQuery(                                        //▼従業員がログインする時、番号とパスワードが正しいかをチェックする
		name = "checkLoginCodeAndPassword", //ﾃｰﾌﾞﾙEmployeeにおいて"e.delete_flag = 0 AND e.code = :code AND e.password = :pass"の件数
		query = "SELECT e FROM Employee AS e WHERE e.delete_flag = 0 AND e.code = :code AND e.password = :pass"
		)
})


//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
@Entity          //エンティティクラスであることを指定
public class Employee{
	@Id        //プライマリキーとなるプロパティかフィールドを指定
	@Column(name = "id")                                                //★カラム属性：id（ﾘｿｰｽ内id番号）
	@GeneratedValue(strategy = GenerationType.IDENTITY) //データベースのidentity列を利用してプライマリキー値を生成する
	private Integer id;
	
	@Column(name = "code", nullable = false, unique = true)  //★カラム属性：code（社員番号） ※unique：一意制約
	private String code;
	
	@Column(name = "name", nullable = false)                     //★カラム属性：name（社員名）
	private String name;
	
	@Column(name = "password", length = 64, nullable = false)//★カラム属性：password（ﾊﾟｽﾜｰﾄﾞ）
	private String password;
	
	@Column(name = "admin_flag", nullable = false)               //★カラム属性：admin_flag（管理者権限ない一般社員=0、管理者=1）
	private Integer admin_flag;
	
	@Column(name = "created_at", nullable = false)               //★カラム属性：created_at（登録日時）
	private Timestamp created_at;
	
	@Column(name = "updated_at", nullable = false)               //★カラム属性：updated_at（更新日時）
	private Timestamp updated_at;
	
	@Column(name = "delete_flag", nullable = false)               //★カラム属性：delete_flag（現役社員=0、削除された社員=1,）
	private Integer delete_flag;


	//^^^^▼ゲッター・セッター^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAdmin_flag() {
		return admin_flag;
	}

	public void setAdmin_flag(Integer admin_flag) {
		this.admin_flag = admin_flag;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

	public Integer getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	
}

