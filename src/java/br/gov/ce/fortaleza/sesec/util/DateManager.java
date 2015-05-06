/**
 * Esta classe possui métodos de conversão e validação de datas.
 */

package br.gov.ce.fortaleza.sesec.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
public class DateManager
{
    /**
     * Método StringToDateSQL() faz a conversão de uma data digitada como
     * string para data do tipo data SQL. Dois parâmetros string são passados:
     * um pattern(padrão) e o valor a ser convertido.
     * @param pattern
     * @param value
     * @return date
     * @throws ParseException
     */
    public static java.sql.Date StringToDateSQL(String pattern, String value) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        java.sql.Date date = new java.sql.Date(format.parse(value).getTime());
        return date;
    }

    /**
     * Método DateSQLToString() faz a conversão de uma data no formato SQL para
     * uma data string com um padrão desejado. SQL. Dois parâmetros são passados:
     * um pattern(padrão - string) e o valor(java.sql.Date) a ser convertido.
     * @param pattern
     * @param value
     * @return date
     */
    public static String DateSQLToString(String pattern, java.sql.Date value)
    {
        SimpleDateFormat formatador = new SimpleDateFormat(pattern);
        String date = (formatador.format(value));
        return date;
    }

    /**
     * Método StringToDateUtil() faz a conversão de uma data digitada como
     * string para data do tipo java.util.Date. Dois parâmetros string são passados:
     * um pattern(padrão) e o valor a ser convertido.
     * @param pattern
     * @param value
     * @return date
     * @throws ParseException
     */
     public static Date StringToDateUtil(String pattern, String value) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        java.sql.Date date = new java.sql.Date(format.parse(value).getTime());
        return date;
    }

     /**
      * Método DateUtilToString() faz a conversão de uma data no formato
      * java.util.Date para uma data string com um padrão desejado.
      * Dois parâmetros são passados: um pattern(padrão - string) e o
      * valor(java.util.Date) a ser convertido.
      * @param pattern
      * @param value
      * @return date
      */
    public static String DateUtilToString(String pattern, Date value)
    {
        SimpleDateFormat formatador = new SimpleDateFormat(pattern);
        String date = (formatador.format(value));
        return date;
    }

    /**
     * Método verificarValidadeDeData verifica se a data (em string) digitada
     * é válida dentro do calendário gregoriado. Em outros termos, este método
     * verifica se a quantidade de dias digitado é válida para aquele mês. Se o
     * mês digitado está entre 1 e 12 e se o ano digitado é válido.
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static boolean verificarValidadeDeData(String dateStr) throws ParseException
    {

        //Cria uma data formatada no formato dd/mm/aaaa
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = new GregorianCalendar();

        //converte a data digitada pelo usuário em uma data dd/mm/aaaa de acordo
        //com o Calendário Gregoriano.
        cal.setTime(df.parse(dateStr));

        //Divide a data digitada em um array de string de acordo com a localização
        //da barra.
        String[] data = dateStr.split("/");

        //Atribui cada item do array a variável correspondente.
        String dia = data[0];
        String mes = data[1];
        String ano = data[2];

        //verifica se o dia digitado é válido para o mês determinado.
        if ( (new Integer(dia)).intValue() != (new Integer(cal.get(Calendar.DAY_OF_MONTH))).intValue() )
        {         
            return false;
        }
        else
            //verifica se o mês digita está entre 1 e 12
            if ( (new Integer(mes)).intValue() != (new Integer(cal.get(Calendar.MONTH)+1)).intValue() )
            {
                                
                return false;
            }
            else
                //verifica se o ano digitado é válido de acordo com calendário
                //gregoriano.
                if ( (new Integer(ano)).intValue() != (new Integer(cal.get(Calendar.YEAR))).intValue() )
                {
                    return false;
                }

         return true;
    }

    /**
     * Retorna a data em dias no ano.
     * @param data
     * @return diaDoAno
     * @throws ParseException
     */
    public static int diaDoAno(String data) throws ParseException
    {
        //Cria uma data no formato dd/mm/aaaa
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = new GregorianCalendar();

        //Converte a data digitada pelo usuário para uma data dd/mm/aaaa
        //de acordo com o Calendário Gregoriano.
        cal.setTime(df.parse(data));

        //Cria uma string com o número de dias do ano.
        String diaDoAno = String.valueOf(cal.get(cal.DAY_OF_YEAR));

        //retorna o número de dias em inteiro.
       return Integer.parseInt(diaDoAno);
    }


}
