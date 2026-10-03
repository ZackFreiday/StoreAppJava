package model.util;

import model.Receipt;

import java.io.*;

public class ReceiptUtils {

    public static void saveToTextFile(Receipt receipt) {
        String fileName = "receipt_" + receipt.getSerialNumber() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(receipt.toString());
        } catch (IOException e) {
            System.err.println("Error writing receipt: " + e.getMessage());
        }
    }

    public static void saveSerialized(Receipt receipt) {
        String fileName = "receipt_" + receipt.getSerialNumber() + ".ser";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(receipt);
        } catch (IOException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }
    }

    public static Receipt loadSerialized(int serialNumber) {
        String fileName = "receipt_" + serialNumber + ".ser";
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Receipt) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
            return null;
        }
    }
}