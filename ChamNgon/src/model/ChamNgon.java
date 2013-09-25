package model;

import android.R.bool;

/**
 * @author Van_Thi
 *
 */
/**
 * @author Van_Thi
 *
 */
public class ChamNgon {
	private int chid;
	private String nDungViet;
	private String nDungAnh;
	private String tacGia;
	private int yeuthich;
	private int cid;
	public ChamNgon(int chid, String nDungViet, String nDungAnh, String tacGia,
			int cid, int yeuthich) {
		super();
		this.chid = chid;
		this.nDungViet = nDungViet;
		this.nDungAnh = nDungAnh;
		this.tacGia = tacGia;
		this.cid = cid;
		this.yeuthich = yeuthich;
	}
	
	public ChamNgon(){
		this(0,"","","",0,0);
	}
	public int getChid() {
		return chid;
	}
	public void setChid(int chid) {
		this.chid = chid;
	}
	public String getnDungViet() {
		return nDungViet;
	}
	public void setnDungViet(String nDungViet) {
		this.nDungViet = nDungViet;
	}
	public String getnDungAnh() {
		return nDungAnh;
	}
	public void setnDungAnh(String nDungAnh) {
		this.nDungAnh = nDungAnh;
	}
	public String getTacGia() {
		return tacGia;
	}
	public void setTacGia(String tacGia) {
		this.tacGia = tacGia;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public int getYeuthich() {
		return yeuthich;
	}

	public void setYeuthich(int yeuthich) {
		this.yeuthich = yeuthich;
	}

	@Override
	public String toString() {
		return "ChamNgon [chid=" + chid + ", nDungViet=" + nDungViet
				+ ", nDungAnh=" + nDungAnh + ", tacGia=" + tacGia
				+ ", yeuthich=" + yeuthich + ", cid=" + cid + "]";
	}

	
	
	
}
