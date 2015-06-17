/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@ManagedBean(name = "reportController")
@SessionScoped
public class ReportController extends HttpServlet {

    JasperPrint rel = null;
    Connection conn;
    EntityManager em;

    public ReportController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ReportController() {
    }

    public void reportOcrrFichaCampo(String protocolo)
            throws ParseException, Exception {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/relatorios/ipe_ocrr_ficha_campo.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=ficha_de_campo_" + protocolo + ".pdf");

        try {

            ReportController rc = new ReportController(
                    Persistence.createEntityManagerFactory("ipePU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = rc.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia o parâmetro strFilter
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("QUERY_PARAMETER", protocolo); //envia o parametro da query para o relatorio
            
            JasperRunManager.runReportToPdfStream(reportStream,
                    servletOutputStream, parametros, this.conn);


            servletOutputStream.flush();
            servletOutputStream.close();

        } catch (JRException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
            System.err.println("Erro: " + e.getMessage()
                    + "\nstringwriter: " + stringWriter.toString()
                    + "\nprintWriter: " + printWriter.toString());

        } finally {
            //encerra a conexão com o banco de dados.
            this.conn.close();

            //envia erro do JSF após completar a geração do relatório
            //avisando o FacesContext que a resposta está completa
            context.responseComplete();

        }
    }
    
    public void getReport(String path, String query_parameter, String file_name)
            throws ParseException, Exception {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream(path);

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=" + file_name + query_parameter + ".pdf");

        try {

            ReportController rc = new ReportController(
                    Persistence.createEntityManagerFactory("ipePU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = rc.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia o parâmetro strFilter
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("QUERY_PARAMETER", query_parameter); //envia o parametro da query para o relatorio
            
            JasperRunManager.runReportToPdfStream(reportStream,
                    servletOutputStream, parametros, this.conn);


            servletOutputStream.flush();
            servletOutputStream.close();

        } catch (JRException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
            System.err.println("Erro: " + e.getMessage()
                    + "\nstringwriter: " + stringWriter.toString()
                    + "\nprintWriter: " + printWriter.toString());

        } finally {
            //encerra a conexão com o banco de dados.
            this.conn.close();

            //envia erro do JSF após completar a geração do relatório
            //avisando o FacesContext que a resposta está completa
            context.responseComplete();

        }
    }
    public void getReport(String path, String file_name)
            throws ParseException, Exception {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream(path);

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=" + file_name + ".pdf");

        try {

            ReportController rc = new ReportController(
                    Persistence.createEntityManagerFactory("ipePU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = rc.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia o parâmetro strFilter
            /*Map<String, String> parametros = new HashMap<String, String>();
             * parametros.put("QUERY_PARAMETER", query_parameter); //envia o parametro da query para o relatorio*/
            
            JasperRunManager.runReportToPdfStream(reportStream,
                    servletOutputStream, new HashMap(), this.conn);


            servletOutputStream.flush();
            servletOutputStream.close();

        } catch (JRException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
            System.err.println("Erro: " + e.getMessage()
                    + "\nstringwriter: " + stringWriter.toString()
                    + "\nprintWriter: " + printWriter.toString());

        } finally {
            //encerra a conexão com o banco de dados.
            this.conn.close();

            //envia erro do JSF após completar a geração do relatório
            //avisando o FacesContext que a resposta está completa
            context.responseComplete();

        }
    }
    
    public void relatorioOcorrencia(String protocolo){
        try {
            getReport("/admin/relatorios/ipe_ocrr_relatorio.jasper", protocolo, "relatorio_ocorrencia_");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void relatorioOcrrRglCurrentDate(){
        try {
            getReport("/admin/relatorios/ipe_graf_regional.jasper", "relatorio_ocorrencia_x_regional_");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String paginaDeRelatorios() {
        return "paginaDeRelatorios";
    }

   
}
