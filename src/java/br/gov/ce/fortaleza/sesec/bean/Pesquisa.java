/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.ce.fortaleza.sesec.bean;

import java.io.Serializable;

/*******************************************************************************
 * @author Thiago Aderaldo Lessa.
 *******************************************************************************/
public class Pesquisa implements Serializable{

    private int opcao;
    private String valorDaPesquisa;

     public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public String getValorDaPesquisa() {
        return valorDaPesquisa;
    }

    public void setValorDaPesquisa(String valorDaPesquisa) {
        this.valorDaPesquisa = valorDaPesquisa;
    }


}
