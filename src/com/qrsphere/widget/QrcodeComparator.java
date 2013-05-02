package com.qrsphere.widget;

import java.util.Comparator;

import com.qrsphere.database.Qrcode;

public class QrcodeComparator  implements Comparator<Qrcode>{



	@Override
	public int compare(Qrcode lhs, Qrcode rhs) {
		int flag = 0;
		if(ScanDetail.getLongFromServerTimeFormat(
				lhs.getQrcodeJSONData().getDateTime())
				==(ScanDetail.getLongFromServerTimeFormat(
						rhs.getQrcodeJSONData().getDateTime())))
			flag = 0;
		if(ScanDetail.getLongFromServerTimeFormat(
				lhs.getQrcodeJSONData().getDateTime())
				>(ScanDetail.getLongFromServerTimeFormat(
						rhs.getQrcodeJSONData().getDateTime())))
			flag = -1;
		if(ScanDetail.getLongFromServerTimeFormat(
				lhs.getQrcodeJSONData().getDateTime())
				<(ScanDetail.getLongFromServerTimeFormat(
						rhs.getQrcodeJSONData().getDateTime())))
			flag = 1;
		
		return flag;
	}

}
