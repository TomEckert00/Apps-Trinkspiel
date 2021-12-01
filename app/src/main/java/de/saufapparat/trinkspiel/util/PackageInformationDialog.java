package de.saufapparat.trinkspiel.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import de.saufapparat.trinkspiel.R;

public class PackageInformationDialog extends AppCompatDialogFragment {

    private Bundle bundle;
    public PackageInformationDialog(Bundle bundle){
        this.bundle=bundle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.package_information_dialog,null);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        String infoLabel = bundle.getString("label");
        String infoText = bundle.getString("text");
        ((TextView) view.findViewById(R.id.morePackageInformations_label)).setText(infoLabel);
        ((TextView) view.findViewById(R.id.morePackageInformations)).setText(infoText);

        return builder.create();
    }

}
