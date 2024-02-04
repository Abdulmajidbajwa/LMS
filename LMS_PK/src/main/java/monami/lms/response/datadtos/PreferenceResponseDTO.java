package monami.lms.response.datadtos;

public class PreferenceResponseDTO {

	private int preferenceId;
	private String preferenceName;
	private String preferenceValue;

	public int getPreferenceId() {
		return preferenceId;
	}
	public void setPreferenceId(int preferenceId) {
		this.preferenceId = preferenceId;
	}
	public String getPreferenceName() {
		return preferenceName;
	}
	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
	}
	public String getPreferenceValue() {
		return preferenceValue;
	}
	public void setPreferenceValue(String preferenceValue) {
		this.preferenceValue = preferenceValue;
	}
}
