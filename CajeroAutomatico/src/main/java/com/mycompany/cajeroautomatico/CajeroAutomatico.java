/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.cajeroautomatico;

import com.mycompany.sistemabancario.BufferMensajeRespuestaSB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

 
public class CajeroAutomatico {

    Scanner reader;
    int cuenta;
    File archivo;
    FileOutputStream fileOutputStream;
    BufferMensajeRespuestaSB bmr;

    public CajeroAutomatico() {
        try {
            bmr = new BufferMensajeRespuestaSB();
            reader = new Scanner(System.in);
            System.out.println("Ingrese su número de cuenta:");
            cuenta = reader.nextInt();
            archivo = new File("C:\\logs.txt");
            fileOutputStream = new FileOutputStream(archivo, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void mostrarMenu() {
        System.out.println("Elija una opción");
        System.out.println("1. Extraer");
        System.out.println("2. Depositar");
        System.out.println("3. Transferir");
        System.out.println("4. Salir");
    }

    public static void main(String[] args) {
        Socket s;
        PrintWriter pw;
        CajeroAutomatico objCajero = new CajeroAutomatico();
        CajeroAutomatico.mostrarMenu();
        int entrada = objCajero.reader.nextInt();

        HiloServidor server = new HiloServidor(objCajero.bmr);
        server.start();

        while (entrada != 4) {
            switch (entrada) {
                case 1: 
                    try {
                    System.out.println("Ingrese el monto a extraer:");
                    float monto = objCajero.reader.nextFloat();

                    s = new Socket(InetAddress.getByName("127.0.0.1"), 25000);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    pw = new PrintWriter(s.getOutputStream(), true);

                    pw.println("extraer," + objCajero.cuenta + "," + monto);
                    String salida = bf.readLine();
                    if (salida.compareTo("1") == 0) {
                        System.out.println("Operación exitosa");
                        objCajero.bmr.SolicitarToken("127.0.0.1:40000");
                        String cadena = "En Cajero Automático: \n";
                        cadena += "Se extrajo de cuenta " + objCajero.cuenta
                                + " la cantidad de pesos " + monto + "\n";
                        objCajero.fileOutputStream.write(cadena.getBytes());
                        objCajero.fileOutputStream.close();
                        objCajero.bmr.DevolverToken();
                    } else {
                        System.out.println("Operación desconocida");
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

                case 2:
                    // Depositar
                   try {
                    System.out.println("Ingrese el monto a depositar:");
                    float monto = objCajero.reader.nextFloat();

                    s = new Socket(InetAddress.getByName("127.0.0.1"), 25000); 
                    BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    pw = new PrintWriter(s.getOutputStream(), true);

                    pw.println("depositar," + objCajero.cuenta + "," + monto);
                    String salida = bf.readLine();
                    if (salida.compareTo("1") == 0) {
                        System.out.println("Operación exitosa");
                        objCajero.bmr.SolicitarToken("127.0.0.1:40000");
                        String cadena = "En Cajero Automático: \n";
                        cadena += "Se deposito de cuenta " + objCajero.cuenta
                                + " la cantidad de pesos " + monto + "\n";
                        objCajero.fileOutputStream.write(cadena.getBytes());
                        objCajero.fileOutputStream.close();
                        objCajero.bmr.DevolverToken();
                    } else {
                        System.out.println("Operación desconocida");
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                }

                   try {
                    System.out.println("Ingrese el monto a depositar:");
                    float monto = objCajero.reader.nextFloat();

                    s = new Socket(InetAddress.getByName("127.0.0.1"), 25001); 
                    BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    pw = new PrintWriter(s.getOutputStream(), true);

                    pw.println("depositar," + objCajero.cuenta + "," + monto);
                    String salida = bf.readLine();
                    if (salida.compareTo("1") == 0) {
                        System.out.println("Operación exitosa");
                        objCajero.bmr.SolicitarToken("127.0.0.1:40000");
                        String cadena = "En Cajero Automático: \n";
                        cadena += "Se deposito de cuenta " + objCajero.cuenta
                                + " la cantidad de pesos " + monto + "\n";
                        objCajero.fileOutputStream.write(cadena.getBytes());
                        objCajero.fileOutputStream.close();
                        objCajero.bmr.DevolverToken();
                    } else {
                        System.out.println("Operación desconocida");
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                } 
                   
                    break;
                case 3:
                    // Transferir 
                   try {
                    System.out.println("Ingrese el monto a transferir:");
                    float monto = objCajero.reader.nextFloat();
                    System.out.println("Ingrese su número de cuenta:");
                    int cuentaDestino = objCajero.reader.nextInt();
                    s = new Socket(InetAddress.getByName("127.0.0.1"), 25000);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    pw = new PrintWriter(s.getOutputStream(), true);

                    pw.println("transferir," + objCajero.cuenta +","+ cuentaDestino +"," + monto);
                    String salida = bf.readLine();
                    if (salida.compareTo("1") == 0) {
                        System.out.println("Operación exitosa");
                        objCajero.bmr.SolicitarToken("127.0.0.1:40000");
                        String cadena = "En Cajero Automático: \n";
                        cadena += "Se transfirio de cuenta " + objCajero.cuenta
                                + " la cantidad de pesos " + monto + "\n";
                        objCajero.fileOutputStream.write(cadena.getBytes());
                        objCajero.fileOutputStream.close();
                        objCajero.bmr.DevolverToken();
                    } else {
                        System.out.println("Operación desconocida");
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CajeroAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                }

                    break;
                default:
                    System.out.println("Opción inválida, ingrese nuevamente.");
            }

            CajeroAutomatico.mostrarMenu();
            entrada = objCajero.reader.nextInt();
        }

        System.exit(0);

    }
}
