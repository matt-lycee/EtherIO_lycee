/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ether_io_controller;
import java.io.IOException;
import java.net.*;
/**
 *
 * @author bts_ciel
 */
public class C_EtherIO {
    //attributs
    private String ServerHostName;
    private String ServerPort;
    
    //methodes
    C_EtherIO(String ip,String portUDP){
        this.ServerHostName = ip;
        this.ServerPort = portUDP;
    }
    
    public void configurerPortSortie(String port){
        //commande : "!XASCII(0)
        int valeur = 0;
        String codeASCII = Character.toString((char)valeur);
        String commande = "!" + port.toUpperCase() + codeASCII;
        this.EnvoyerSocket(commande);
    }
    
    public void configurerPortEntree(String port){
        int valeur = 255;
        String CodeASCII = Character.toString((char)valeur);
        String commande = "!" + port.toUpperCase() + CodeASCII;
        this.EnvoyerSocket(commande);
    }
    
    public void ecrirPort(String port, int valeur){
        String CodeASCII = Character.toString((char)valeur);
        String commande = port.toUpperCase()+ CodeASCII;
        this.EnvoyerSocket(commande);
    }
    
    public int lirePort(String port){
        int valeurLue =-1;
        String commande = port.toLowerCase();
        byte[] reponse = this.EnvoyerSocketEtReceptionnerReponse(commande);
        valeurLue = (int)reponse[1] & 0xFF;
        return valeurLue;
    }
            
            
    protected void EnvoyerSocket(String message){
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP_Serveur = InetAddress.getByName(this.ServerHostName);
            byte[] sendData = new byte[1024];
            sendData = message.getBytes("ISO-8859-1");
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IP_Serveur, Integer.parseInt(this.ServerPort));
            clientSocket.send(sendPacket);
            clientSocket.close();
        }
        catch(UnknownHostException ex){
            System.err.println(ex);
        }
        catch(IOException ex){
            System.err.println(ex);
        }
    }
    protected byte[] EnvoyerSocketEtReceptionnerReponse(String message){
        byte[] Reponse = null;
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP_Serveur = InetAddress.getByName(this.ServerHostName);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            sendData = message.getBytes("ISO-8859-1");
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IP_Serveur, Integer.parseInt(this.ServerPort));
            clientSocket.send(sendPacket);
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.print("Attente de la reponse...");
            clientSocket.setSoTimeout(5000);
            try{
                clientSocket.receive(receivePacket);
                Reponse = receivePacket.getData();
            }
            catch(SocketTimeoutException ste){
                System.out.println("Temps d'attente depasse: Packet perdu");
            }
            clientSocket.close();
        }
        catch (UnknownHostException ex){
            System.err.println(ex);
        }
        catch (IOException ex){
            System.err.println(ex);
        }
       return Reponse;
    }
}
