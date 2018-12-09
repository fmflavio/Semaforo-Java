import java.util.Random;

/**
 * 
 * Trabalho 02 - Projeto de SO - Semaforo
 * Sistemas de Computacao 2018.2
 * Prof. Dr. Antonio Guto Rocha
 * @author Flávio Miranda de Farias
 *
 */
enum CordoSemaforo{
	VERMELHO, AZUL, VERDE 
}
//Semáforo automatizado
public class Semaforo implements Runnable {
	    private Thread thread;// contém a thread que executa a simulação
	    private CordoSemaforo cor;// Armazena a cor do sinal
	    boolean parar = false;// configura a ativação para interromper
	    boolean mudar = false;// para indicar que o sinal mudou
	    static int tempoVermelho, tempoVerde, tempoAzul; //tempo do semáforo
	    static int alteracao = 6; //número de alterações de cores
	    private Random random;

	    public Semaforo() {
	        cor = CordoSemaforo.VERMELHO;
	        thread = new Thread(this);
	        random = new Random();
	        thread.start();
	    }
	    //inicia o semáforo
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
	    //Espera até uma mudança de sinal ocorrer
	    synchronized void aguardarMudancadeCor() {
	        try {
	            while (!mudar) {
	                System.out.println("Aguardando a mudança de cor ");
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
	    //Interrompe o semáforo
	    synchronized void cancelar() {
	        parar = true;
	    }
	    //Inicia o sistema
	    public static void main(String[] args) {
	    	if (!args[0].isEmpty()) {
				alteracao = Integer.parseInt(args[0])*3;//insere o numero de ciclos
			} 
	    	System.out.println("Iniciando o Sistema de Semáforo\n");
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
