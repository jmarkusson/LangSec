package backEnd;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */  
    private RandomAccessFile file;

    /**
     * Creates a Wallet object
     *
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet () throws Exception {
	this.file = new RandomAccessFile(new File("backEnd/wallet.txt"), "rw");
    }

    /**
     * Gets the wallet balance. 
     *
     * @return                   The content of the wallet file as an integer
     */
    public int getBalance() throws IOException {
	this.file.seek(0);
	return Integer.parseInt(this.file.readLine());
    }

    /**
     * Sets a new balance in the wallet
     *
     * @param  newBalance          new balance to write in the wallet
     */
    public void setBalance(int newBalance) throws Exception {
	this.file.setLength(0);
	String str = Integer.valueOf(newBalance).toString()+'\n'; 
	this.file.writeBytes(str); 
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
	this.file.close();
    }

    /**
     * Checks if the wallet has enough balance to withdraw the given value
     * @param valueToWithdraw the amount to withdraw
     * @return true if the wallet has enough balance, false otherwise
     */
    public boolean safeWithdraw(int valueToWithdraw) throws Exception {
        FileChannel channel = this.file.getChannel();

        try (FileLock lock = channel.lock()){
                int balance = this.getBalance();
            if (balance < valueToWithdraw)
                return false;
            this.setBalance(balance - valueToWithdraw);
            return true;
            }
        
    }
}
