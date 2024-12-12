package com.mamboflix.model.interest;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryResponse implements Parcelable{
	private String description;
	private String created_at;
	private String id;
	private String title;
	private String status;
	private boolean isSelected = false;

	protected CategoryResponse(Parcel in) {
		description = in.readString();
		created_at = in.readString();
		id = in.readString();
		title = in.readString();
		status = in.readString();
	}

	public CategoryResponse() {

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(description);
		dest.writeString(created_at);
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {
		@Override
		public CategoryResponse createFromParcel(Parcel in) {
			return new CategoryResponse(in);
		}

		@Override
		public CategoryResponse[] newArray(int size) {
			return new CategoryResponse[size];
		}
	};



	@Override
	public String toString(){
		return this.title.equalsIgnoreCase("All")?this.title:this.title;
	}
}
