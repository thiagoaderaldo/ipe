/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.primefaces.util;

/**
 *
 * @author thiago
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class FileUploadView {

    private String destination="/images/";
    
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Passou por handleFileUpload");
        if (file != null) {
            
            FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // Do what you want with the file        
            try {
                copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {


            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}