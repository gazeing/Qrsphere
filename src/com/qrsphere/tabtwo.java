package com.qrsphere;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class tabtwo extends Activity {
	private List<String> data = new ArrayList<String>();
	protected List<String> getData() {
		return data;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lView = new ListView(this);
		data.add("FAQ");
		data.add("Acknowledgement");
		data.add("Terms and Licenses");
		data.add("About Us");
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData());
		lView.setAdapter(adp);
		setContentView(lView);
		lView.setBackgroundResource(R.drawable.background);
		lView.setOnItemClickListener(new OnItemClickListener() {  
	      	  
            @Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                
            	switch (position){
            	case 0:
            		showFAQ();
            		break;
            	case 1:
            		showAcknowledgement();
            		break;
            	case 2:
            		showTerms();
            		break;
            	case 3:
            		showAbout();
            		break;
            	default:
            		break;
            	}

            	
            }  
        }); 
	}
	protected void showAbout() {
		String str = "This demo is designed to show how we work with QR code business.";
		showDialogText(str,"About Us: ");
		
	}
	protected void showTerms() {
		String str = "\"License\" shall mean the terms and conditions for use, reproduction, and distribution as defined by Sections 1 through 9 of this document.";

			str +="\n\n\"Licensor\" shall mean the copyright owner or entity authorized by the copyright owner that is granting the License.";

			str +="\n\n\"Legal Entity\" shall mean the union of the acting entity and all other entities that control, are controlled by, or are under common control with that entity. For the purposes of this definition, \"control\" means (i) the power, direct or indirect, to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.";

			str +="\n\n\"You\" (or \"Your\") shall mean an individual or Legal Entity exercising permissions granted by this License.";

			str +="\n\n\"Source\" form shall mean the preferred form for making modifications, including but not limited to software source code, documentation source, and configuration files.";

			str +="\n\n\"Object\" form shall mean any form resulting from mechanical transformation or translation of a Source form, including but not limited to compiled object code, generated documentation, and conversions to other media types.";

			str +="\n\n\"Work\" shall mean the work of authorship, whether in Source or Object form, made available under the License, as indicated by a copyright notice that is included in or attached to the work (an example is provided in the Appendix below).";

			str +="\n\n\"Derivative Works\" shall mean any work, whether in Source or Object form, that is based on (or derived from) the Work and for which the editorial revisions, annotations, elaborations, or other modifications represent, as a whole, an original work of authorship. For the purposes of this License, Derivative Works shall not include works that remain separable from, or merely link (or bind by name) to the interfaces of, the Work and Derivative Works thereof.";

			str +="\n\n\"Contribution\" shall mean any work of authorship, including the original version of the Work and any modifications or additions to that Work or Derivative Works thereof, that is intentionally submitted to Licensor for inclusion in the Work by the copyright owner or by an individual or Legal Entity authorized to submit on behalf of the copyright owner. For the purposes of this definition, \"submitted\" means any form of electronic, verbal, or written communication sent to the Licensor or its representatives, including but not limited to communication on electronic mailing lists, source code control systems, and issue tracking systems that are managed by, or on behalf of, the Licensor for the purpose of discussing and improving the Work, but excluding communication that is conspicuously marked or otherwise designated in writing by the copyright owner as \"Not a Contribution.\"";

			str +="\n\n\"Contributor\" shall mean Licensor and any individual or Legal Entity on behalf of whom a Contribution has been received by Licensor and subsequently incorporated within the Work.";



		showDialogText(str,"Terms & Licenses");
		
	}
	protected void showAcknowledgement() {
		String str = "Steven Xu would like to thank the many people who have have written leaflets and books about Malvern, and supported the QR Code project that is now underway. We would also like to thank the numerous people who have given their time to tell us about different aspects of this wonderful place.";
		showDialogText(str,"Acknowledgement");
	}
	protected void showFAQ() {
		String str = "Q How large is QR Code?";
			str+= "\nA The size of QR Code depends on a module size and a symbol version. The module refers to the black and white dots that make up the QR Code. The symbol version determines the data capacity. The area of a Version 1 QR Code is made up of 21 modules per side. Each higher version number is made up of 4 additional modules. The highest version number is Version 40. For example, a Version 2 QR Code would be composed of a 25 by 25 module matrix. If the module size is 0.5 mm (19.7 mils) square, the size of QR Code will be 12.5 mm (0.5\") square. (QR Code requires a four module wide quiet zone around the symbol.)";
		showDialogText(str,"Acknowledgement");
		
	}
	protected void showDialogText(String text,String title){
		InstructionsDialog(text,title);
	}
	protected void InstructionsDialog(String text,String title){

		  AlertDialog.Builder ad = new AlertDialog.Builder(this);
		  ad.setIcon(R.drawable.ic_launcher);
		  ad.setTitle(title);
		  LayoutInflater linf = LayoutInflater.from(this);
		  final View inflator = linf.inflate(R.layout.scorll, null);
		  ad.setView(inflator);

		  ad.setPositiveButton("OK", 
		    new android.content.DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int arg1) {
		      // OK, go back to Main menu
		     }
		    }
		   );

		   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
		    public void onCancel(DialogInterface dialog) {
		     // OK, go back to Main menu   
		    }}
		   );
		   
			  TextView tv =(TextView)(inflator.findViewById(R.id.TextView01));
			  if (tv!=null)
				tv.setText(text);

		  ad.show();

		 }
	
}
