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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import compute.Compute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ComputeServicos {
    public static void main(String args[]) {

        String upload = "upload";
        String download = "download";
        String ponto = "ponto";
        String caminho_cliente;
        String caminho_servidor;
        String id_funcionario;
        byte[] arquivo;

        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0],Integer.parseInt(args[1]));
            System.out.println("<< Cliente busca pelo nome de host e porta do servidor >>");
            System.out.println("<< Cliente passa um nome de serviço a ser buscado no registro do servidor >>");		
            Compute comp = (Compute) registry.lookup(name);

            if(upload.equals(args[2])){
                caminho_cliente = args[3];  // <caminho_no_cliente/arquivoexistente_cliente>
                caminho_servidor = args[4]; // <C:/ServerStorage/nome_novoarquivo>

                try{
                    File caminhoarquivo_cliente = new File(caminho_cliente);
                    arquivo = new byte[(int) caminhoarquivo_cliente.length()];
                    FileInputStream in = new FileInputStream(caminhoarquivo_cliente);	
                    System.out.println("Fazendo upload do arquivo...");		
                    in.read(arquivo, 0, arquivo.length);
                    in.close();
	
                    CompartilhamentoArquivo task = new CompartilhamentoArquivo(args[2],arquivo,caminho_cliente,caminho_servidor);
                    System.out.println(" << Cliente invoca o método remoto no servidor >> ");
                    byte[] retorno = comp.executeTask(task);    // essa operação não retorna nada do servidor
                } catch (IOException e) {
                    System.err.println("Erro: Não foi possível ler o arquivo");
                }		
            }

            if(download.equals(args[2])){
		        caminho_cliente = args[4];  // <caminho_no_cliente/novonome_arquivobaixado>
		        caminho_servidor = args[3]; // <C:/ServerStorage/arquivoexistente_servidor>

                arquivo = new byte[0];  // array vazio, já que não vamos enviar nada ao servidor

                CompartilhamentoArquivo task = new CompartilhamentoArquivo(args[2],arquivo,caminho_cliente,caminho_servidor);
                System.out.println(" << Cliente invoca o método remoto no servidor >> ");
                byte[] retorno = comp.executeTask(task);    // retorna um vetor com os dados do arquivo do servidor
                
                // salva o arquivo recebido no cliente
                try{
                    File novoarquivo_cliente = new File(caminho_cliente);
                    FileOutputStream out = new FileOutputStream(novoarquivo_cliente);
                    out.write(retorno);
                        out.flush();
                    out.close();
                    System.out.println("Arquivo salvo");
                } catch (Exception e) {
                    System.err.println("Erro: Não foi possível gravar o arquivo");
                }
            }

            if(ponto.equals(args[2])){
                id_funcionario = args[3];   // identificador do funcionário para registrar seu ponto
                SistemaPonto task = new SistemaPonto(id_funcionario);
                System.out.println(" << Cliente invoca o método remoto no servidor >> ");
                String resposta = comp.executeTask(task);
                System.out.println(resposta);
            }


        } catch (Exception e) {
            System.err.println("Compute exception:");
            e.printStackTrace();
        }
    }    
}
