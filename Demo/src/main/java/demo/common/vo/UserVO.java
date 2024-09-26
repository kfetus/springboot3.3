package demo.common.vo;

public class UserVO {

	private String userId;
	private String userPass;
	private String salt;
	private String userName;
	private int userNo;
	private String userIp;
	private String descrip;
	private String grade;
	
	private String hpNo;
	private String email;
	private String zoneCode;
	private String roadAddr;
	private String jibunAddr;
	private String detailAddr;
	private String regId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getHpNo() {
		return hpNo;
	}
	public void setHpNo(String hpNo) {
		this.hpNo = hpNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getRoadAddr() {
		return roadAddr;
	}
	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}
	public String getJibunAddr() {
		return jibunAddr;
	}
	public void setJibunAddr(String jibunAddr) {
		this.jibunAddr = jibunAddr;
	}
	public String getDetailAddr() {
		return detailAddr;
	}
	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", userPass=" + userPass + ", salt=" + salt + ", userName=" + userName
				+ ", userNo=" + userNo + ", userIp=" + userIp + ", descrip=" + descrip + ", grade=" + grade + ", hpNo="
				+ hpNo + ", email=" + email + ", zoneCode=" + zoneCode + ", roadAddr=" + roadAddr + ", jibunAddr="
				+ jibunAddr + ", detailAddr=" + detailAddr + ", regId=" + regId + "]";
	}

}
