/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.report;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.jsf.controller.AtendimentosController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
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

    private static final String imagensPATH = File.separator + "admin"
            + File.separator + "relatorios" + File.separator + "imagens";
    private static final String logoPATH = imagensPATH + File.separator + "logo_sesec_300x102_RGB.jpg";
    private static final String barra_cabecalhoPATH = imagensPATH + File.separator + "barra_cabecalho.png";
    private static final String barra_inferior_direitaPATH = imagensPATH + File.separator + "barra_inferior_direita.png";
    private static final String barra_inferior_esquerdaPATH = imagensPATH + File.separator + "barra_inferior_esquerda.png";
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
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("QUERY_PARAMETER", protocolo); //envia o parametro da query para o relatorio

            BufferedImage logo = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(logoPATH)));

            BufferedImage barra_cabecalho = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_cabecalhoPATH)));

            BufferedImage barra_inferior_direita = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_direitaPATH)));

            BufferedImage barra_inferior_esquerda = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_esquerdaPATH)));

            parametros.put("logo", logo); //envia o parametro da query para o relatorio*/
            parametros.put("barra_cabecalho", barra_cabecalho); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_direita", barra_inferior_direita); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_esquerda", barra_inferior_esquerda); //envia o parametro da query para o relatorio*/

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
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("QUERY_PARAMETER", query_parameter); //envia o parametro da query para o relatorio
            
            BufferedImage logo = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(logoPATH)));

            BufferedImage barra_cabecalho = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_cabecalhoPATH)));

            BufferedImage barra_inferior_direita = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_direitaPATH)));

            BufferedImage barra_inferior_esquerda = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_esquerdaPATH)));

            parametros.put("logo", logo); //envia o parametro da query para o relatorio*/
            parametros.put("barra_cabecalho", barra_cabecalho); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_direita", barra_inferior_direita); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_esquerda", barra_inferior_esquerda); //envia o parametro da query para o relatorio*/

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
            Map<String, Object> parametros = new HashMap<String, Object>();
            //parametros.put("QUERY_PARAMETER", query_parameter); //envia o parametro da query para o relatorio*/
            //ImageIcon img = new ImageIcon("/admin/relatorios/imagens/logo_sesec_300x102_RGB.jpg");
            //ImageIcon img = createImageIcon("/home/thiago/NetBeansProjects/ipe/web/admin/relatorios/imagens/logo_sesec_300x102_RGB.jpg","imagem de teste");
            BufferedImage logo = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(logoPATH)));

            BufferedImage barra_cabecalho = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_cabecalhoPATH)));

            BufferedImage barra_inferior_direita = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_direitaPATH)));

            BufferedImage barra_inferior_esquerda = ImageIO.read(
                    new File(context.getExternalContext().getRealPath(barra_inferior_esquerdaPATH)));

            parametros.put("logo", logo); //envia o parametro da query para o relatorio*/
            parametros.put("barra_cabecalho", barra_cabecalho); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_direita", barra_inferior_direita); //envia o parametro da query para o relatorio*/
            parametros.put("barra_inferior_esquerda", barra_inferior_esquerda); //envia o parametro da query para o relatorio*/

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

    public void relatorioOcorrencia(String protocolo) {
        try {
            getReport("/admin/relatorios/ipe_ocrr_relatorio.jasper", protocolo, "relatorio_ocorrencia_");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void relatorioOcrrRglCurrentDate() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional.jasper", "relatorio_ocorrencia_x_regional_");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reportTipologia() {
        try {
            getReport("/admin/relatorios/tipologia/ipe_graf_tipologia.jasper", "relatorio_tipologia_");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl1T() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_1T.jasper", "relatorio_qtd_ATD_x_RGL_1T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl2T() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_2T.jasper", "relatorio_qtd_ATD_x_RGL_2T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl3T() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_3T.jasper", "relatorio_qtd_ATD_x_RGL_3T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl4T() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_4T.jasper", "relatorio_qtd_ATD_x_RGL_4T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl1S() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_primeiroS.jasper", "relatorio_qtd_ATD_x_RGL_1S");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void reportQtdAtdXRgl2S() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_segundoS.jasper", "relatorio_qtd_ATD_x_RGL_2S");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void report_qtd_ATD_x_RGL_Y() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_years.jasper", "relatorio_qtd_ATD_x_RGL_Y");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void report_qtd_ATD_x_TPL_Y() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_year.jasper", "relatorio_qtd_ATD_x_TPL_Y");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void report_QTD_ATD_x_TPL_1S() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_primeiroS.jasper", "relatorio_QTD_ATD_x_TPL_1S");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void report_QTD_ATD_x_TPL_2S() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_segundoS.jasper", "relatorio_QTD_ATD_x_TPL_2S");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_TPL_1T() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_1T.jasper", "relatorio_qtd_ATD_x_TPL_1T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_TPL_2T() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_2T.jasper", "relatorio_qtd_ATD_x_TPL_2T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_TPL_3T() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_3T.jasper", "relatorio_qtd_ATD_x_TPL_3T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_TPL_4T() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_4T.jasper", "relatorio_qtd_ATD_x_TPL_4T");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_TPL_MES() {
        try {
            getReport("/admin/relatorios/ipe_graf_tipologia_mes.jasper", "relatorio_qtd_ATD_x_TPL_mes");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void report_QTD_ATD_x_RGL_MES() {
        try {
            getReport("/admin/relatorios/ipe_graf_regional_mes.jasper", "relatorio_qtd_ATD_x_RGL_mes");
        } catch (ParseException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String paginaDeRelatorios() {
        return "paginaDeRelatorios";
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected ImageIcon createImageIcon(String path,
            String description) {
        //java.net.URL imgURL = getClass().getResource(path);
        java.net.URL imgURL = null;
        try {
            imgURL = new URL(path);
        } catch (MalformedURLException ex) {
            System.out.println("Erro ao tentar obter o recurso da String path");
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
