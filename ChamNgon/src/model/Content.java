package model;

/**
 * @author Van_Thi
 *
 */
public class Content {
	private int cid;
	private String ten;
	public Content(int cid, String ten) {
		super();
		this.cid = cid;
		this.ten = ten;
	}
	public Content(){
		this(0,"");
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	@Override
	public String toString() {
		return "Content [cid=" + cid + ", ten=" + ten + "]";
	}
	
	
}	
