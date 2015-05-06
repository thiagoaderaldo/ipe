/* 
 * Este arquivo contém javascripts que podem ser usados por toda aplicação.
 * Caso se faça necessário utilizar um destes scripts em alguma página deve-se
 * usar o caminho da seguinte forma:
 * <script
 *   src="#{facesContext.externalContext.requestContextPath}/scripts/scripts.js"
 *   type="text/javascript">
 * </script>
 */
function Formatadata(Campo, teclapres)
{
        var tecla = teclapres.keyCode;
        var vr = new String(Campo.value);
        vr = vr.replace("/", "");
        vr = vr.replace("/", "");
        vr = vr.replace("/", "");
        tam = vr.length + 1;
        if (tecla != 8 && tecla != 8)
        {
                if (tam > 0 && tam < 2)
                        Campo.value = vr.substr(0, 2) ;
                if (tam > 2 && tam < 4)
                        Campo.value = vr.substr(0, 2) + '/' + vr.substr(2, 2);
                if (tam > 4 && tam < 7)
                        Campo.value = vr.substr(0, 2) + '/' + vr.substr(2, 2) + '/' + vr.substr(4, 7);
        }
}

function isNumberKey(evt)
{
        var charCode = (evt.which) ? evt.which : event.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57))
           return false;
        return true;
}

function enableRadioWithNumberOnly(evt,elementId)
{
  var obj = isNumberKey(evt);
  if(obj==true)
  {
       document.getElementById(elementId).checked=true;
       return true;
  }
  else
       return false;
}

function enableRadio(elementId)
{
   document.getElementById(elementId).checked=true;
   return true;
}

function copiaValorDoCheckBoxDeSubCategorias(caixa)
{
    var formulario=document.formCadLivros;

    if(caixa.checked)
    {
            if(formulario.subcategorias.value == '')
                    formulario.subcategorias.value += caixa.value;
            else
                    formulario.subcategorias.value += ';' + caixa.value;
    }
    else
    {
            if(formulario.subcategorias.value.indexOf(';',0)!=-1)
                    if(formulario.subcategorias.value.indexOf(caixa.value,0)==0)
                            formulario.subcategorias.value = formulario.subcategorias.value.toString().replace(caixa.value+';',"");
                    else
                            formulario.subcategorias.value = formulario.subcategorias.value.toString().replace(';' + caixa.value,"");
            else
                    formulario.subcategorias.value = formulario.subcategorias.value.toString().replace(caixa.value,"");
    }
}

function atualizarCheckBoxDeSubcategorias()
{
    
    var formulario=document.formCadLivros;
    //var formulario2=document.formCadLivros + document.getElementById('subViewSubCDDs:tabelaDeSubCDDs');
    
    var formulario2=document.formCadLivros;
    var conj = formulario.subcategorias.value.split(';');
    //var value = '';
    for(i=0;i<formulario2.length;i++)
    {
        if(formulario2.elements[i].type=="checkbox" && formulario.subcategorias.value.indexOf(formulario2.elements[i].value,0)!=-1)
        {
                for(j=0;j<conj.length;j++)
                {
                        if(formulario2.elements[i].value == conj[j])
                        {
                                formulario2.elements[i].checked=true;
                                //value = value + formulario2.elements[i].value + '\n';
                        }
                }
        }
    }
    alert('Formulario 2: ' + formulario2.toString());
    //alert('Valores: \n' + value);
}