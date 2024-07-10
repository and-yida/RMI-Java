/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package client;

import compute.Task;
import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SistemaPonto implements Task<String>, Serializable {

    private final String registro = "C:/ServerStorage/registro.csv";
    private final String id;
    
    /**
     * Construção da tarefa
     */
    public SistemaPonto(String id) {
        this.id = id;
    }

    /**
     * Realiza as operações
     */
    public String execute() {
        try {
            return Registrar(id, registro);
        } catch (IOException ex) {
            Logger.getLogger(SistemaPonto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(); // se algo deu errado, retorna string vazia
    }

    /**
     *
     */
    public static String Registrar(String funcionario, String registro) throws IOException{
        try{

            // abre o arquivo de registro no servidor
            File arquivo = new File(registro);

            String linhas;
            String[] campos_tabela = new String[0];
            Scanner leitor = new Scanner(arquivo);
            String ultimo_registro = "";
            String resposta;
            LocalDateTime horario = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String horario_formatado = horario.format(formato);

            // percorre linha por linha do arquivo e coloca cada campo do CSV (separado por ",") em uma posição do vetor campos_tabela
            while(leitor.hasNext()){
                linhas = leitor.nextLine();
                campos_tabela = linhas.split(";");
            }
            leitor.close();

            // procura pelo último registro do referido funcionário e obtém o campo seguinte que responde: se o último registro foi de entrada ou saída
            for(int i=campos_tabela.length-1; i>=0; i--){
                if(campos_tabela[i].equals(funcionario)){
                    ultimo_registro = campos_tabela[i+1];
                    break;
                }
            }

            // se o último registro foi entrada, agora é saída
            if(ultimo_registro.equals("entrada")){
                FileWriter escritor = new FileWriter(registro, true);
                escritor.write(funcionario + ";" + "saida" + ";" + horario_formatado + "\n");
                escritor.flush();
                escritor.close();
                
                resposta = "Saída registrada.";
                System.out.println("<< Servidor executou registro de ponto >>");
                return resposta;
            }
            // se o último registro foi saída, agora é entrada
            else if(ultimo_registro.equals("saida")){
                FileWriter escritor = new FileWriter(registro, true);
                escritor.write(funcionario + ";" + "entrada" + ";" + horario_formatado + "\n");
                escritor.flush();
                escritor.close();
                
                resposta = "Entrada registrada.";
                System.out.println("<< Servidor executou registro de ponto >>");
                return resposta;
            }
            // caso seja o primeiro registro do funcionario, é uma entrada
            else{
                FileWriter escritor = new FileWriter(registro, true);
                escritor.write(funcionario + ";" + "entrada" + ";" + horario_formatado + "\n");      
                escritor.flush();
                escritor.close();
                resposta = "Entrada registrada.";
                System.out.println("<< Servidor executou registro de ponto >>");
                return resposta;
            }	

        }catch (IOException e) {
            e.printStackTrace();
	}

        return new String();    // se algo deu errado, retorna string vazia
    }
        
}
