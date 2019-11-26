package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
@Table(name = "reports")//Entityクラスと対応するデータベースのテーブル名をreportsに指定する
@NamedQueries({
	@NamedQuery(
			name = "getAllReports",
			query = "SELECT r FROM Report AS r ORDER BY r.id DESC" //ﾃｰﾌﾞﾙreportsを全てselect, IDが大きい順にソートし→カラム名rとする
			),
	@NamedQuery(
			name = "getReportsCount",
			query = "SELECT COUNT(r) FROM Report AS r" //ﾃｰﾌﾞﾙreportsの全てのレコード件数をカウントし→カラム名rとする
			),
})

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
@Entity
public class Report {
	@Id                           //プライマリキーとなるプロパティかフィールドを指定
	@Column(name = "id")                                            //★カラム属性：id（ﾘｿｰｽ内id番号）
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //データベースのidentity列を利用してプライマリキー値を生成する
	private Integer id;
	
	@ManyToOne             //@MaynToOneアノテーションで「多対1」であることを表す。
	@JoinColumn(name = "employee_id", nullable = false)      //★結合列employee_idを作成
	private Employee employee; //※オブジェクト名はemployee。授業員の情報を表すプロパティ。
	
	@Column(name = "report_date", nullable = false)            //★カラム属性report_dateを作成  ※いつの仕事の日報か示すプロパティ。
	private Date report_date;
	
	@Column(name = "title", length = 255, nullable = false)    //★カラム属性titleを作成
	private String title;
	
	@Lob //これを指定することで、改行もデータベースに保存されます。
	@Column(name = "content", nullable = false)                 //★カラム属性contentを作成  ※日報の内容（コメント）を記述するプロパティ。
	private String content;
	
	@Column(name = "created_at", nullable = false)              //★カラム属性created_atを作成
	private Timestamp created_at;
	
	@Column(name = "updated_at", nullable = false)              //★カラム属性updated_atを作成
	private Timestamp updated_at;
	
	
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^▼以下ｹﾞｯﾀｰｾｯﾀｰ
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public Date getReport_date() {
		return report_date;
	}
	
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
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
}