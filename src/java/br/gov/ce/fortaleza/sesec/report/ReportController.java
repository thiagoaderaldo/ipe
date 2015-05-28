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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import br.gov.ce.fortaleza.sesec.jpa.controller.TipologiasJpaController;
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
    TipologiasJpaController tipologiasJpaController;
    AtendimentosJpaController atendimentosJpaController;
    Connection conn;
    EntityManager em;

    public ReportController() {
    }

    public void dailyReport()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/relatorios/atendimentos-daily-report.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-relatorio-diario-" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    /*public void dailyReportHTMLFile()
     * throws ParseException, Exception {
     * FacesContext context = FacesContext.getCurrentInstance();
     * HttpServletResponse response = (HttpServletResponse) context.
     * getExternalContext().getResponse();
     * HttpServletRequest request = (HttpServletRequest) context.
     * getExternalContext().getRequest();
     * 
     * ServletOutputStream servletOutputStream = response.getOutputStream();
     * 
     * //pega o caminho do arquivo .jasper da aplicação
     * InputStream reportStream = context.getExternalContext().
     * getResourceAsStream("/admin/relatorios/atendimentos-daily-report.jasper");
     * 
     * //envia a resposta com o MIME Type PDF
     * response.setContentType("application/html");
     * 
     * 
     * //obtém o horário em que o relatório foi gerado.
     * String hora = "yyyyMMdd-HHmmss";
     * SimpleDateFormat df = new SimpleDateFormat(hora);
     * df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
     * String data = df.format(new Date());
     * 
     * //força a abertura de download no navegador
     * response.setHeader("Content-disposition",
     * "attachment;filename=iguana-relatorio-diario-" + data + ".html");
     * 
     * try {
     * 
     * atendimentosJpaController = new AtendimentosJpaController(
     * Persistence.createEntityManagerFactory("iguanaPU"));
     * 
     * //Obtém a conexão com o banco de dados para repassar ao relatório
     * //através da camada JPA 2.0.
     * this.em = atendimentosJpaController.getEntityManager();
     * em.getTransaction().begin();
     * this.conn = em.unwrap(java.sql.Connection.class);
     * 
     * //envia para o navegador o PDF gerado
     * //JasperRunManager.runReportToPdfStream(reportStream,
     * //      servletOutputStream, new HashMap(), this.conn);
     * JasperPrint report = JasperFillManager.fillReport(
     * reportStream,
     * new HashMap(), this.conn);
     * 
     * JRHtmlExporter htmlExporter = new JRHtmlExporter();
     * 
     * // display stack trace in the browser
     * StringWriter stringWriter = new StringWriter();
     * PrintWriter printWriter = new PrintWriter(stringWriter);
     * 
     * 
     * response.setContentType("text/html");
     * 
     * request.getSession().setAttribute(
     * ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
     * report);
     * htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,
     * report);
     * htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER,
     * printWriter);
     * htmlExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
     * "ISO-8859-1");
     * /*
     * aqui é mapeado para o servlet do JasperReport, para que ao gerar o
     * html não renderize as imagens em branco, pois os espaços em branco,
     * são imagens em branco que ele adiciona!
     * Basta adicionar no web.xml a chamada ao servlet que existe no pacote:
     *
     * <servlet>
     * <servlet-name>ImageServlet</servlet-name>
     * <servlet-class>net.sf.jasperreports.j2ee.servlets.ImageServlet</servlet-class>
     * </servlet>
     * 
     * <servlet-mapping>
     * <servlet-name>ImageServlet</servlet-name>
     * <url-pattern>/image.servlet</url-pattern>
     * </servlet-mapping>
     *
     
     htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
     request.getContextPath() + "/image.servlet?image=");
     htmlExporter.exportReport();
    
     servletOutputStream.flush();
     servletOutputStream.close();
    
     } catch (JRException e) {
     } catch (IOException e) {
     } catch (Exception e) {
     // display stack trace in the browser
     StringWriter stringWriter = new StringWriter();
     PrintWriter printWriter = new PrintWriter(stringWriter);
     e.printStackTrace(printWriter);

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
     }*/
    
    public void weeklyReport()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/relatorios/atendimentos-weekly-report.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-relatorio-semanal-" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void monthReport()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/relatorios/atendimentos-month-report.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-relatorio-mensal-" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void yearReport()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/relatorios/atendimentos-year-report.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-relatorio-anual-" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportByResponsible()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportByResponsible.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-relatorio-porResponsavel-" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportByResponsibleOrderByDay()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportByResponsibleOrderByDay.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=reportByResponsible.pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportByResponsibleOrderByWeek()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportByResponsibleOrderByWeek.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=reportByResponsible.pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportByResponsibleOrderByMonth()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportByResponsibleOrderByMonth.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=reportByResponsible.pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportByResponsibleOrderByYear()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportByResponsibleOrderByYear.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=reportByResponsible.pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportBySubject()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportBySubject.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");


        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=reportBySubject.pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportBySubjectDaily()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportBySubject-Daily.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-rel-assunto" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public void reportBySubjectWeek()
            throws ParseException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.
                getExternalContext().getResponse();

        ServletOutputStream servletOutputStream = response.getOutputStream();

        //pega o caminho do arquivo .jasper da aplicação
        InputStream reportStream = context.getExternalContext().
                getResourceAsStream("/admin/estatisticas/reportBySubject-Week.jasper");

        //envia a resposta com o MIME Type PDF
        response.setContentType("application/pdf");

        //obtém o horário em que o relatório foi gerado.
        String hora = "yyyyMMdd-HHmmss";
        SimpleDateFormat df = new SimpleDateFormat(hora);
        df.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
        String data = df.format(new Date());

        //força a abertura de download no navegador
        response.setHeader("Content-disposition",
                "attachment;filename=iguana-rel-assunto" + data + ".pdf");

        try {

            atendimentosJpaController = new AtendimentosJpaController(
                    Persistence.createEntityManagerFactory("iguanaPU"));

            //Obtém a conexão com o banco de dados para repassar ao relatório
            //através da camada JPA 2.0.
            this.em = atendimentosJpaController.getEntityManager();
            em.getTransaction().begin();
            this.conn = em.unwrap(java.sql.Connection.class);

            //envia para o navegador o PDF gerado
            //JasperRunManager.runReportToPdfStream(reportStream,
            //      servletOutputStream, new HashMap(), this.conn);
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

    public String paginaDeRelatorios() {
        return "paginaDeRelatorios";
    }
}
