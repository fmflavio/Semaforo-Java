import java.util.Random;

/**
 * 
 * Trabalho 02 - Projeto de SO - Semaforo
 * Sistemas de Computacao 2018.2
 * Prof. Dr. Antonio Guto Rocha
 * @author Fl�vio Miranda de Farias
 *
 */
enum CordoSemaforo{
	VERMELHO, AZUL, VERDE 
}
//Sem�foro automatizado
public class Semaforo implements Runnable {
	    private Thread thread;// cont�m a thread que executa a simula��o
	    private CordoSemaforo cor;// Armazena a cor do sinal
	    boolean parar = false;// configura a ativa��o para interromper
	    boolean mudar = false;// para indicar que o sinal mudou
	    static int tempoVermelho, tempoVerde, tempoAzul; //tempo do sem�foro
	    static int alteracao = 6; //n�mero de altera��es de cores
	    private Random random;

	    public Semaforo() {
	        cor = CordoSemaforo.VERMELHO;
	        thread = new Thread(this);
	        random = new Random();
	        thread.start();
	    }
	    //inicia o sem�foro
	    @Override
	    public void run() {
	        // inicia a luz
	        while (!parar) {
	            try {
	                switch (cor) {
	                case VERDE:
	                	tempoVerde = (random.nextInt(9)*1000);//tempo em segundos
	                    Thread.sleep(tempoVerde);//adormece a thread
	                    break;
	                case VERMELHO:
	                	tempoVermelho = (random.nextInt(9)*1000);
	                    Thread.sleep(tempoVermelho);
	                    break;
	                case AZUL:
	                	tempoAzul = (random.nextInt(9)*1000);
	                    Thread.sleep(tempoAzul);
	                    break;
	                }
	            } catch (InterruptedException e) {
	            	System.out.println(e);
	            }
	            mudarCor();
	        }
	    }
	    //Muda a cor do sinal
	    synchronized void mudarCor() {
	        switch (cor) {
	        case VERMELHO:
	            cor = CordoSemaforo.AZUL;
	            break;
	        case AZUL:
	            cor = CordoSemaforo.VERDE;
	            break;
	        case VERDE:
	            cor = CordoSemaforo.VERMELHO;
	        }
	        mudar = true;
	        notify(); //sinaliza que mudou a cor
	    }
	    //Espera at� uma mudan�a de sinal ocorrer
	    synchronized void aguardarMudancadeCor() {
	        try {
	            while (!mudar) {
	                System.out.println("Aguardando a mudan�a de cor ");
	                wait(); //espera o sinal mudar
	            }
	            mudar = false;
	        } catch (InterruptedException e) {
	        	System.out.println(e);
	        }
	    }
	    //Retorna a cor atual
	    synchronized CordoSemaforo getColor() {
	        return cor;
	    }
	    //Interrompe o sem�foro
	    synchronized void cancelar() {
	        parar = true;
	    }
	    //Inicia o sistema
	    public static void main(String[] args) {
	    	if (!args[0].isEmpty()) {
				alteracao = Integer.parseInt(args[0])*3;//insere o numero de ciclos
			} 
	    	System.out.println("Iniciando o Sistema de Sem�foro\n");
	        Semaforo thr = new Semaforo();
	        for (int i = 0; i < alteracao; i++) {
	            if (thr.cor == CordoSemaforo.AZUL) {
	            	System.out.println("==>" + thr.getColor()+" ("+(tempoAzul/1000)+" segundos)");
				} else {
					if (thr.cor == CordoSemaforo.VERDE) {
						System.out.println("==>" + thr.getColor()+" ("+(tempoVerde/1000)+" segundos)");
					} else {
						if (thr.cor == CordoSemaforo.VERMELHO) 
							System.out.println("==>" + thr.getColor()+" ("+(tempoVermelho/1000)+" segundos)");
					}
				}
	            thr.aguardarMudancadeCor();
	        }
	        thr.cancelar();
	        System.out.println("Fim.");
	    }
	}
