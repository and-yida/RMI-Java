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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompartilhamentoArquivo implements Task<byte[]>, Serializable {

    private final byte[] arquivo;
    private final String operacao;
    private final String cliente;
    private final String servidor;
    
    /**
     * Construção da tarefa
     */
    public CompartilhamentoArquivo(String operacao, byte[] arquivo, String cliente, String servidor) {
        this.arquivo = arquivo;
        this.cliente = cliente;
        this.servidor = servidor;
        this.operacao = operacao;	
    }

    /**
     * Realiza as operações
     */
    public byte[] execute() {
        try {
            return servico(operacao, arquivo, cliente, servidor);
        } catch (IOException ex) {
            Logger.getLogger(CompartilhamentoArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0]; // se algo deu errado, retorna array vazio
    }

    /**
     *
     */
    public static byte[] servico(String operacao, byte[] arquivo, String cliente, String servidor)throws IOException{
        byte[] resultado = new byte[0]; // inicializa array vazio
        try {
            switch (operacao) {
                case "upload":
                    resultado = upload(arquivo, cliente, servidor);
                    break;
                case "download":
                    resultado = download(servidor);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println("Erro durante a operação: " + e.getMessage());
        }
        return resultado;
    }

    public static byte[] upload(byte[] arquivo, String cliente, String servidor) throws IOException{
        try{
            File novoarquivo_servidor = new File(servidor);
            FileOutputStream out = new FileOutputStream(novoarquivo_servidor);
            out.write(arquivo);
                out.flush();
            out.close();
	    } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("<< Arquivo enviado >>");

        byte[] retorno = new byte[0];   // retorna array vazio, já que a operação não retorna nada
        return retorno;
    }

    public static byte[] download(String servidor) throws IOException{
	    try {
            File arquivo_servidor = new File(servidor);			
            byte[] arquivobaixado = new byte[(int)arquivo_servidor.length()];
            FileInputStream in = new FileInputStream(arquivo_servidor);
            in.read(arquivobaixado, 0, arquivobaixado.length);
            in.close();

            System.out.println("<< Arquivo do servidor obtido >>");
            
            return arquivobaixado;  // retorna array de bytes contendo o arquivo do servidor
	        } catch (IOException e) {	
            e.printStackTrace();
	    }		

        return new byte[0];     // se não deu certo
    }
        
}
