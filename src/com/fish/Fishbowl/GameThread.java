package com.fish.Fishbowl;

import java.security.SecureRandom;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;
import android.os.Vibrator;

import com.games.Base.BaseGameThread;
import com.games.Base.Collisions;
import com.games.Sprites.Button;
import com.games.Sprites.Pic;
import com.games.Sprites.Sprite;
import com.games.Sprites.Text;

/*
 * TODO: 
 * 
 * - Introducción
 * 		- help
 * 		- calibración disimulada
 * 
 * - Jugabilidad
 * 		- Aceleración de subida?
 * 		- Que las pirañas no tiendan a subir a la superfície
 * 
 * - Premios
 * 		- Aletas o velocidad
 * 		- Bombonas de oxígeno
 * 		- Pirañas con inteligencia
 * 
 * - Dibujos 
 * 		- Gato muerto blanco con piraña en la boca
 * 
 * 	
 * 
 */

public class GameThread extends BaseGameThread {

	// Objetos
	public Sounds snd;
	public Images img;
	private Vector<Pez> peces = new Vector<Pez>();
	private Vector<Piranha> piranhas = new Vector<Piranha>();
	private Vector<Bubble> bubbles = new Vector<Bubble>();
	private Gato gato;
	private Shark shark = null;
	//private Scheduler sched;
	private Text ttoast;

	private SecureRandom sr;
	private Paint panel;
	private Paint pWhite;
	private Paint pOxigenBar;
	private Paint pBlue;
	private Pic marker;
	private Pic olas;
	private Pic gameover;
	private Pic aureola;
	private Button btnDown;
	private Button btnSkip;
	
	private boolean isCreatingPiranhas;
	private boolean isCreating;
	public boolean isControllingGato;
	private boolean tutorialTriggerDown;
	private boolean tutorialTriggerEat;
	private boolean isScoreOn;

	// Variables
	private static int score;
	private int piranha_score;
	private static int lives;
	public int screen;
	

	// CONSTANTES

	// Estados globales del juego

	public final static int SCREEN_INIT = 1;
	public final static int SCREEN_CALIBRATING = 2;
	public final static int SCREEN_TUTORIAL_DOWN = 3; // Hasta tocar fondo
	public final static int SCREEN_TUTORIAL_LATERAL = 4; // Hasta comer un pez
	public final static int SCREEN_TUTORIAL_OK = 5;
	public final static int SCREEN_TUTORIAL_FISHES = 6;
	public final static int SCREEN_GAME_READY = 7;
	public final static int SCREEN_GAME = 8;
	public final static int SCREEN_GAMEOVER = 9;

	
	// Schedules
	private final static int MSG_DURATION = 10;
	private final static int MSG_NONE = 0;
	private final static int SCHED_MSG_CALIBRATE = 1;
	private final static int SCHED_MSG_TUTORIAL_DOWN = 2;
	private final static int SCHED_MSG_TUTORIAL_EAT = 3;
	private final static int SCHED_MSG_TUTORIAL_HELP = 4;
	private final static int SCHED_MSG_INIT_GAME = 5;
	private int tutorialMsg = MSG_NONE;
	
	public final static String TOAST_CALIBRATE = "Put your device horizontal and tap to calibrate.";
	public final static String TOAST_TUTORIAL_DOWN = "Hold the button to dive down to the bottom.";
	public final static String TOAST_TUTORIAL_EAT1 = "You can tilt the phone to move sideways."; //TABLET: cambiar phone por tablet
	public final static String TOAST_TUTORIAL_EAT2 = "let's try to eat a fish.";
	public final static String TOAST_HELP = "Good. Now tap the screen to see the fish types.";
	public final static String TOAST_HELP2 = "you will find on the fishbowl.";
	public final static String TOAST_GAME = "Tap the screen to start!!";
	public final static String TOAST_EXIT = "Press back again to exit.";
	
	
	// Puntuación que se gana al comer cada tipo de bicho
	public final static int SCORE_PAYASO = 10;
	public final static int SCORE_GLOBO = 10;
	public final static int SCORE_GATO = 50;
	public final static int SCORE_STAR = 20;
	public final static int SCORE_PIRANHA = 20;

	// Posicion de los botones
	public static int BUTTON_Y;
	public static int BUTTON_X1;
	public static int BUTTON_X2;
	public static int BUTTON_SKIP_X;
	public static int BUTTON_SKIP_Y;
	public static int SCORE_X;
	public static int SCORE_Y;

	// Constantes relativas a la pantalla
	public static int SURFACE;
	public static int FISH_SURFACE;
	public static int PIRANHA_SURFACE;
	public static int FISH_DOWN_MARGIN;
	public static int PIRANHA_DOWN_MARGIN;
	public static int PIRANHA_DIRECTION_VARIATION;
	public static int SHARK_DIRECTION_VARIATION;

	// Probabilidad de cambio de rumbo
	public final static int PIRANHA_ENTROPY = 300; // probability of change the
													// rumbo 1/300
	public final static int SHARK_ENTROPY = 500; // probability of change the
													// rumbo 1/500

	// Cantidad maxima de bichos
	private static int MAX_FISH;
	private static int TOP_MAX_PIRANHAS;
	public static int MAX_PIRANHAS; // valor inicial variable

	// Constantes del aire
	public static int MAX_AIR;
	public static int GATO_ROJO_AIR;
	public static int PEZGLOBO_AIR;

	// Velocidad gatuna
	private static int CAT_SPEED;

	// probabilidad de aparición de un nuevo pez, TABLET: ajustar probabilidades
	private final static int PROBABILITY_NEW_PIRANHA = 400;
	private final static int PROBABILITY_NEW_FISH = 200;
	private final static int PROBABILITY_NEW_STAR = 800;
	private final static int PROBABILITY_NEW_GLOBO = 250;
	private final static int PROBABILITY_NEW_BUBBLE = 10;
	private final static int PROBABILITY_NEW_SHARK = 200;

	// Initialization

	private void randomInit() {
		SecureRandom rseed = new SecureRandom();
		byte[] seed = new byte[40];
		rseed.nextBytes(seed);
		sr = new SecureRandom(seed);
	}

	public GameThread(SurfaceHolder sh, Context ctx) {
		super(sh, ctx);
		isProcessing = false; // iniciamos sin procesamiento, hasta que pulse el
								// usuario y pase a estado
		snd = new Sounds(ctx);
		img = new Images(ctx);

		randomInit();
	}

	@Override
	public void preInitializeGame() {
		Log.d("fishbowl", "preInitializeGame()");
		score = 0;
		piranha_score = 0;
		lives = 7;
		//sched = new Scheduler();
	}

	@Override
	public void initializeGame() {
		// Log.d("fishbowl","initializeGame()");
		snd.load();
		img.loadBackgrounds();
		
		// Creacion de sprites
		


		btnDown = new Button(ctx, 0, 0);
		btnDown.load(R.drawable.down2);

		btnSkip = new Button(ctx, 0, 0);
		btnSkip.load(R.drawable.skip_tutorial);

		if (!isReloading)
			screenRelativeConstants();

		gameover = new Pic(ctx);
		gameover.load(R.drawable.gameover);

		aureola = new Pic(ctx);
		aureola.load(R.drawable.aureolab);

		marker = new Pic(ctx);
		marker.load(R.drawable.gatovida);
		olas = new Pic(ctx);
		olas.load(R.drawable.olas);

		pWhite = new Paint();
		pWhite.setAntiAlias(true);
		pWhite.setARGB(255, 255, 255, 255);
		pWhite.setTextSize(30);

		pOxigenBar = new Paint();
		pOxigenBar.setAntiAlias(true);
		pOxigenBar.setARGB(255, 0xff, 0x00, 0x00);

		pBlue = new Paint();
		pBlue.setAntiAlias(true);
		pBlue.setARGB(255, 0x25, 0xc5, 0xf9);


		
		setScreen(GameThread.SCREEN_INIT);

	}

	public void screenRelativeConstants() { // Relativize
		gato = new Gato(ctx, img.canvasWidth, img.canvasHeight);
		
		int gatoHeight = gato.getHeight();
		// int gatoWidth = gato.getWidth();

		SURFACE = img.canvasHeight / 8 - (int) (gatoHeight / 1.5f);
		FISH_SURFACE = SURFACE + (gatoHeight + gatoHeight / 2);
		PIRANHA_SURFACE = FISH_SURFACE;
		FISH_DOWN_MARGIN = SURFACE / 2;
		PIRANHA_DOWN_MARGIN = FISH_DOWN_MARGIN;
		PIRANHA_DIRECTION_VARIATION = SURFACE;
		SHARK_DIRECTION_VARIATION = SURFACE * 2;
		MAX_AIR = img.canvasHeight - SURFACE - (int) (gatoHeight / 1.5f);
		GATO_ROJO_AIR = MAX_AIR / 2;
		PEZGLOBO_AIR = MAX_AIR / 4;
		MAX_FISH = SURFACE / 10;
		TOP_MAX_PIRANHAS = MAX_FISH;
		if (!isReloading)
			MAX_PIRANHAS = img.canvasHeight / 100 / 3; // o cases inicial 2
		CAT_SPEED = MAX_PIRANHAS; // 2

		btnDown.y = img.canvasHeight - btnDown.getHeight();
		btnDown.x = img.canvasWidth - btnDown.getWidth();

		btnSkip.y = img.canvasHeight - btnSkip.getHeight();
		btnSkip.x = 10;

		/*
		 * BUTTON_Y = img.canvasHeight-gatoWidth-SURFACE*3; BUTTON_X1 =
		 * gato.getWidth()/2; BUTTON_X2 = img.canvasWidth-gatoWidth-gatoWidth/3;
		 */

		
			/*  Velocidad del gato
			 * 
			 
			 * 1280 x 720 6 GalaxyIII
			 *  800 x 480 4 Desire HD
			 *  480 x 320 4 Magic
			 *  
			 *  
			 *  
			 * y=w*h/100
			 *  
			 *  9216  6       9216*3/3840
			 *  3840 3-4
			 *  1536  x = 1536*3/3840
			 *  
			 *  
			 */
		
		int gato_speed = 4;
		
		if (img.canvasCenterWidth <= 600)
			gato_speed = 4;
		else
			gato_speed = 6;
		
		int gato_super_speed = gato_speed*2;
		
		
		gato.setSpeeds(gato_speed, gato_super_speed);
		gato.initialPosition();
		gato.doRespirar();
		
		ttoast = new Text(50,img.canvasHeight-gato.getHeight()-(int)(gato.getHeight()*1.5));
		ttoast.addText(TOAST_CALIBRATE);
		ttoast.addText(TOAST_TUTORIAL_DOWN);
		ttoast.addText(TOAST_TUTORIAL_EAT1);
		ttoast.addText(TOAST_TUTORIAL_EAT2);
		ttoast.addText(TOAST_HELP);
		ttoast.addText(TOAST_HELP2);
		ttoast.addText(TOAST_GAME);
		ttoast.addText(TOAST_EXIT);
		
		

		
		
		/* tamaño del texto
		 * 
		 * 1280 x 720 6 GalaxyIII
		 *  800 x 480 30 Desire HD
		 *  480 x 320 4 Magic
		 * 
		 * 
		 */
		 
		
		int text_size = 30;
		if (img.canvasHeight <= 400)
			text_size = 17;
		else if (img.canvasHeight >= 600)
			text_size = 40;
		 
		
		ttoast.setTextSize(text_size); // TODO: relativizar
		ttoast.setColor(200, 255, 255);
		
		// panel
		panel = new Paint();
		panel.setAntiAlias(true);
		panel.setARGB(255, 255, 0, 0);
		panel.setTextSize(text_size+5);
		
		
		SCORE_X = img.canvasCenterWidth + (img.canvasCenterWidth/4);
		SCORE_Y = SURFACE/2 + text_size+6;
		
		if (img.canvasHeight <= 400)
			SCORE_X = img.canvasCenterWidth+70;
		
	}

	// Helpers
	
	public boolean existGlobo() {
		for (Pez p : peces) {
			if (p.getFishtype() == Pez.FISH_GLOBO)
				return true;
		}
		return false;
	}

	public void doToast(String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public void gatoPierdeVida() {
		snd.sndMorir.play();
		gato.setStat(Gato.MUERTO);
		lives--;
		gato.doRespirar();

		Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(400);

		if (lives == 0)
			setScreen(SCREEN_GAMEOVER);

	}

	public void piranhaCome(Pez p) {
		// Incrementos score
		switch (p.getFishtype()) {
		
			case Pez.FISH_PAYASO:
				piranha_score += SCORE_PAYASO;
				break;
			case Pez.FISH_GLOBO:
				piranha_score += SCORE_GLOBO;
				break;
			case Pez.FISH_STAR:
				piranha_score += SCORE_STAR;
				break;

		}
	}

	public void gatoCome(Pez p) {
		snd.sndComer.play();

		switch (gato.getStat()) {
		case Gato.COMIENDO:
		case Gato.NADANDO:
			gato.setStat(Gato.COMIENDO);
			break;

		case Gato.COMIENDO_ROJO:
		case Gato.AHOGANDOSE:
			gato.setStat(Gato.COMIENDO_ROJO);
			break;

		}

		if (tutorialTriggerEat)
			setScreen(SCREEN_TUTORIAL_OK);

	
		if (isScoreOn) 
			// Incrementos score
			switch (p.getFishtype()) {
			case Pez.FISH_PAYASO:
				score += SCORE_PAYASO;
				break;
			case Pez.FISH_GLOBO:
				score += SCORE_GLOBO;
				gato.globoAir();
				break;
			case Pez.FISH_STAR:
				score += SCORE_STAR;
				gato.setSuperGuerrero();
				break;
			// case Pez.FISH_PIRANHA:
			// score += SCORE_PIRANHA;
	
			}
		

		// Premios
		switch (score) {
		case 200:
			MAX_PIRANHAS++;
			break;
		case 300:
			lives++;
			MAX_PIRANHAS++;
			break;
		case 1000:
			lives++;
			MAX_PIRANHAS++;
			break;
		case 1500:
			MAX_PIRANHAS++;
			break;
		case 2000:
			lives++;
			MAX_PIRANHAS++;
			break;
		}
	}

	public void newBubble(Sprite sp, int sz, boolean prob) {
		if (!gato.isSubiendoRecto()) {
			if (!prob || sr.nextInt(PROBABILITY_NEW_BUBBLE) == 1) {
				Bubble b = new Bubble(this.ctx, sp.x + (sp.getWidth() / 2) - sz
						+ sr.nextInt(sz), sp.y + sp.getHeight() / 2 + 20, sz);
				bubbles.add(b);
			}
		}
	}

	public boolean hayEstrellas() {
		for (Pez p : peces) {
			if (p.getFishtype() == Pez.FISH_STAR)
				return true;
		}
		return false;
	}

	public void piranhaMira(Piranha p, Sprite p2) {
		if (p.isDerecha()) {
			if (p2.getCenterX() < p.getCenterX())
				p.turn();
		} else {
			if (p2.getCenterX() > p.getCenterX())
				p.turn();
		}
	}

	// Engine

	@Override
	public void pauseEngine() {
		this.setRunning(false);
		this.snd.stopMusics();
		this.snd.stopSounds();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		} catch (Exception e) {
		}
	}

	@Override
	public void doIA() {

		// APARICION DE NUEVOS PECES TODO: prefijar en una app para tablet y
		// otra para telefono

		/*
		 * El tiburon no está listo todavía
		 * 
		 * // aparece tiburón if ((shark == null) &&
		 * sr.nextInt(PROBABILITY_NEW_SHARK) == 1) { shark = new
		 * Shark(ctx,img.canvasWidth,img.canvasHeight); }
		 */

		if (isCreating) {

			// aparece piraña
			if (isCreatingPiranhas && piranhas.size() < MAX_PIRANHAS) {
				if (sr.nextInt(PROBABILITY_NEW_PIRANHA) == 1)
					piranhas.add(new Piranha(ctx, img.canvasWidth,
							img.canvasHeight));
			}

			// siempre ha de haber un pez en la pantalla
			if (peces.size() == 0)
				peces.add(new Pez(this.ctx, Pez.FISH_PAYASO, img.canvasWidth,
						img.canvasHeight));

			// aparicion de peces
			if (peces.size() < MAX_FISH)
				if (sr.nextInt(PROBABILITY_NEW_FISH) == 1)
					peces.add(new Pez(this.ctx, Pez.FISH_PAYASO,
							img.canvasWidth, img.canvasHeight));

			// aparicion de estrellas de mar
			if (piranhas.size() > 2 && !hayEstrellas())
				if (sr.nextInt(PROBABILITY_NEW_STAR) == 1)
					peces.add(new Pez(ctx, Pez.FISH_STAR, img.canvasWidth, img.canvasHeight));

			// aparición de peces globo
			if (peces.size()>0 &&  !existGlobo() && piranhas.size()>0 && sr.nextInt(PROBABILITY_NEW_GLOBO) == 1)
				peces.add(new Pez(ctx, Pez.FISH_GLOBO, img.canvasWidth,	img.canvasHeight));

			// Mover peces que no se salgan de la pantalla
			for (Pez p : peces) {
				p.doStep();
				Collisions.insideScreen(p, img.canvasWidth, img.canvasHeight);
			}

		}

		// Mover tiburon
		if (shark != null)
			shark.doStep();

		// Mover pirañas y que no se salgan de la pantalla
		for (Piranha p : piranhas) {
			p.doStep();
			Collisions.insideScreen(p, img.canvasWidth, img.canvasHeight);

			// Si la piraña tiene detrás un pez o un gato, se gira hacia él.

			// una pizca de ia
			for (Pez pp : peces) {
				if (pp.getCenterY() - pp.getHeight() > p.getCenterY()
						&& pp.getCenterY() + pp.getHeight() < p.getCenterY())
					piranhaMira(p, pp);
			}

		}

	}

	@Override
	public void doPhysics() {

		/*
		//Scheduler
		sched.doTick();
		
		// Caducan los mensajes por pantalla
		if (sched.isTimeout(SCHED_MSG_CALIBRATE)) {
			tutorialMsg = MSG_NONE;
		} else if (sched.isTimeout(SCHED_MSG_TUTORIAL_DOWN)) {
			tutorialMsg = MSG_NONE;
		} else if (sched.isTimeout(SCHED_MSG_TUTORIAL_EAT)) {
			tutorialMsg = MSG_NONE;
		} else if (sched.isTimeout(SCHED_MSG_TUTORIAL_HELP)) {
			tutorialMsg = MSG_NONE;
		} else if (sched.isTimeout(SCHED_MSG_INIT_GAME)) {
			tutorialMsg = MSG_NONE;
		} */
	

		// Gestión de estados del gato

		if (gato.y <= SURFACE) { // El gato está en la superficie
			snd.sndBubbles.stop();

			if (gato.getAir() < GATO_ROJO_AIR)
				snd.sndRespirar.play();

			switch (gato.getStat()) {
			case Gato.AHOGANDOSE:
				gato.setStat(Gato.NADANDO);
				break;
			case Gato.COMIENDO_ROJO:
				gato.setStat(Gato.COMIENDO);
				break;
			}

			gato.doRespirar();

			// air = (isTablet?MAX_AIR_TABLET:MAX_AIR);
			// if (gato.getStat() != Gato.NADANDO) // Esto lanza un continuo
			// cambio de estado ...
			// gato.setStat(Gato.NADANDO);

		} else { // El gato está bajo el agua

			if (gato.isVisible())
				newBubble(gato, Bubble.SZ_SMALL, true);

			// Gato Muerto
			if (gato.getStat() == Gato.MUERTO) {
				if (gato.isVisible())
					newBubble(gato, Bubble.SZ_BIG, true);

				// esta muerto y ha tocado fondo
				if (gato.y >= img.canvasHeight - gato.getHeight() - 10) {

					gato.setStat(Gato.NADANDO);
					gato.initialPosition();
					gato.show();

				} else { // Esá muerto pero todabía no ha tocado fondo TODO:
							// usar tiempo en lugar de descenso, porque ahora no
							// se ve el gato acer.
					gato.goDown();
					gato.doStep();
					return;
				}

			} else { // El gato está vivo y bajo el agua

				// Según el nivel de aire matar el gato, ponerlo rojo, o ponerlo
				// normal

				gato.decAir();

				if (gato.getAir() <= 0) {
					if (gato.isVisible())
						newBubble(gato, Bubble.SZ_BIG, true);
					gatoPierdeVida();
				}

				else if (gato.getAir() == GATO_ROJO_AIR)
					gato.setStat(Gato.AHOGANDOSE);

				// else if (gato.getAir() >= GATO_ROJO_AIR && )
				// gato.setStat(Gato.NADANDO);
			}
		}

		// Avanza el gato
		gato.doStep();
		
		// En el tutorial, hay que tocar el fondo
		if (tutorialTriggerDown && gato.isOnTheBottom())
			setScreen(SCREEN_TUTORIAL_LATERAL);

		// Si el gato toca un pez, se lo come
		Vector<Pez> comer = new Vector<Pez>();
		for (Pez p : peces) {
			int c = Collisions.colisionPlanoPlano(gato, p);
			if (c != Collisions.COLLISION_NONE) {
				comer.add(p);
				gatoCome(p);
				newBubble(p, Bubble.SZ_MEDIUM, false);
			}
		}

		Vector<Bubble> bubbleToExpliot = new Vector<Bubble>();
		for (Bubble b : bubbles) {
			if (b.y <= SURFACE + gato.getHeight() - 23)
				bubbleToExpliot.add(b);
			else
				b.doStep();
		}
		for (Bubble b : bubbleToExpliot) {
			bubbles.remove(b);
		}

		Vector<Piranha> piranyasMuertas = new Vector<Piranha>();

		// Las pirañas comen
		for (Piranha p : piranhas) {

			// Si la piraña toca al gato
			int c = Collisions.colisionPlanoPlano(gato, p);
			if (c != Collisions.COLLISION_NONE) {

				if (gato.isSuperGuerrero()) {
					snd.sndComer.play();
					gato.setStat(Gato.COMIENDO);
					score += SCORE_PIRANHA;
					piranyasMuertas.add(p);
					newBubble(p, Bubble.SZ_MEDIUM, false);

				} else {
					piranhaMira(p, gato);
					p.doComer(true);
					gato.hide();
					gatoPierdeVida();
					piranha_score += 50;
				}
			}

			// Si la piraña toca un pez
			for (Pez pp : peces) {
				c = Collisions.colisionPlanoPlano(pp, p);
				if (c != Collisions.COLLISION_NONE) {
					comer.add(pp);
					newBubble(p, Bubble.SZ_MEDIUM, false);
					p.doComer(false);
					piranhaCome(pp);
				}
			}
		}

		// Quitar peces comidos
		for (Pez p : comer)
			peces.remove(p);

		// Quitar pirañas comidas por gato super guerrero
		for (Piranha p : piranyasMuertas)
			piranhas.remove(p);

		// Las burbujas suben y se eliminan las que llegan a la superficie
		Vector<Bubble> eliminar = new Vector<Bubble>();
		for (Bubble b : bubbles) {
			b.doStep();
			if (b.y <= GameThread.SURFACE - Bubble.SPEED)
				eliminar.add(b);
		}
		for (Bubble b : eliminar)
			bubbles.remove(b);

	}

	@Override
	public void doDraw(Canvas c) {
		// Background

		c.drawBitmap(img.bg, 0, 0, null);
		c.save();
		c.rotate((float) 0, 0, img.canvasHeight);

		// // Orden de dibujado

		// Puntos
		if (isScoreOn)
			c.drawText("Score: " + String.valueOf(score), SCORE_X, SCORE_Y, panel);
		
		// Vidas
		for (int i = 0; i < lives; i++)
			marker.doDraw(c, 30 + (i * (marker.getWidth()+3)), 5);

		// Peces
		for (Pez p : peces)
			p.doDraw(c);

		// Gato
		
		if (gato.isVisible()) {
			gato.doDraw(c);
			if (gato.isSuperGuerrero())
				aureola.doDraw(c, gato.x - 10, gato.y - gato.getHeight() + 35);
		}
		
		// Pirañas
		for (Piranha p : piranhas)
			p.doDraw(c);

		// Tiburon
		if (shark != null)
			shark.doDraw(c);

		// Burbujas
		for (Bubble b : bubbles)
			b.doDraw(c);

		// Linea de superficie
		// c.drawLine(0, SUPERFICIE+gato.getHeight()-20, img.canvasWidth,
		// SUPERFICIE+gato.getHeight()-23, pWhite);

		// Barra de Oxigeno
		if (gato.getAir() > MAX_AIR / 1.6)
			pOxigenBar.setARGB(255, 0xff, 0xff, 0xff); // Primera mitad
		else if (gato.getAir() > MAX_AIR / 2)
			pOxigenBar.setARGB(255, 0xed, 0x46, 0x0c); // ed460c
		else if (gato.getAir() > MAX_AIR / 3)
			pOxigenBar.setARGB(255, 0xff, 0, 0); // rojo oscuro
		else
			pOxigenBar.setARGB(255, 0xa0, 0x0c, 0xed); // lila a00ced

		c.drawRect(0, img.canvasHeight - gato.getAir(), 10, img.canvasHeight, pOxigenBar);

		// Botones
		btnDown.doDraw(c);
		btnSkip.doDraw(c);

		if (ttoast.isEnabled())
			ttoast.doDraw(c);
		
		if (screen == SCREEN_GAMEOVER)
				gameover.doDraw(c, img.canvasWidth / 2 - 100, img.canvasHeight / 2 - 10);

		
	}

	
	public void setScreen(int s) {
	
		// Control de flujo
		
		switch (s) {
	
			case SCREEN_INIT: 
				isProcessing = false;
				isCreating = false;
				isCreatingPiranhas = false;
				isControllingGato = false;
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isScoreOn = false;
				gato.show();

				ttoast.start(Text.TEXT_FIXED, 0, 1);
				
				//sched.setTimeout(MSG_DURATION, SCHED_MSG_CALIBRATE);
				btnDown.hide();
				btnSkip.hide();
				break;
	
			case SCREEN_CALIBRATING:
				isCreating = false;
				isCreatingPiranhas = false;
				isProcessing = false;
				isControllingGato = false;
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isScoreOn = false;
				gato.show();
				ttoast.disable();
				btnDown.hide();
				break;
	
			case SCREEN_TUTORIAL_DOWN:
				isProcessing = true;
				isCreating = true;
				isCreatingPiranhas = false;
				isControllingGato = true;
				tutorialTriggerDown = true;
				tutorialTriggerEat = false;
				isScoreOn = false;
				btnDown.show();
				btnSkip.show();
				
				
				ttoast.start(Text.TEXT_SLOW, 1, 1);
				
				//sched.setTimeout(MSG_DURATION, SCHED_MSG_TUTORIAL_DOWN);
				gato.initialPosition();
				break;
	
			case SCREEN_TUTORIAL_LATERAL:
				isProcessing = true;
				isCreating = true;
				isCreatingPiranhas = false;
				isControllingGato = true;
				tutorialTriggerDown = false;
				tutorialTriggerEat = true;
				isScoreOn = false;
				gato.show();
				btnDown.show();
				btnSkip.show();
	
				ttoast.start(130, 2, 2);
				
				//sched.setTimeout(MSG_DURATION, SCHED_MSG_TUTORIAL_EAT);
				gato.initialPosition();
				break;
				
			case SCREEN_TUTORIAL_OK:
				
				ttoast.start(Text.TEXT_FIXED, 4, 2);
				
				bubbles.clear();
				
				isProcessing = false;
				isCreating = true;
				isCreatingPiranhas = false;
				isControllingGato = false;
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isScoreOn = false;
				btnDown.hide();
				btnSkip.hide();
				gato.hide();
				break;
	
		case SCREEN_TUTORIAL_FISHES:
				isProcessing = false;
				isCreating = true;
				isCreatingPiranhas = false;
				isControllingGato = false;
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isScoreOn = false;
				btnDown.hide();
				btnSkip.hide();
				gato.hide();
				ttoast.disable();
				//sched.setTimeout(MSG_DURATION, SCHED_MSG_TUTORIAL_HELP);
				//doToast(TOAST_HELP);
				break;
				
				
			case SCREEN_GAME_READY:
				lives = 7;
				isProcessing = isCreating = isCreatingPiranhas = false;
				btnDown.show();
				btnSkip.hide();
				tutorialTriggerDown = tutorialTriggerEat = isControllingGato = false;
				gato.initialPosition();
				gato.show();
				
				ttoast.start(Text.TEXT_FIXED, 6, 1);
				break;
	
			case SCREEN_GAME:
				snd.playBackground();
				ttoast.disable();
				gato.initialPosition();
				gato.show();
				lives = 7;
				isProcessing = isCreating = isCreatingPiranhas = isControllingGato = isScoreOn = true;
				btnDown.show();
				btnSkip.hide();
				tutorialTriggerDown = tutorialTriggerEat =  false;
	
				
				// peces.removeAllElements();
				// score = 0;
				/*
				lives = 7;
				isProcessing = isCreating = isCreatingPiranhas = true;
				btnDown.show();
				btnSkip.hide();
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isControllingGato = true;
				isScoreOn = true;
				snd.playBackground();
				gato.initialPosition();
				gato.show();
				tutorialMsg = SCHED_MSG_INIT_GAME;
				//sched.setTimeout(MSG_DURATION, SCHED_MSG_INIT_GAME);
				*/
				
				break;
	
			case SCREEN_GAMEOVER:
				isProcessing = false;
				isCreatingPiranhas = false;
				isControllingGato = false;
				tutorialTriggerDown = false;
				tutorialTriggerEat = false;
				isScoreOn = true;
				snd.playGameOver();
				btnDown.hide();
				btnSkip.hide();
				gato.hide();
				cleanFishes();
				break;
		}
		img.selectBackground(s);
		screen = s;
	}

	protected void cleanFishes() {
		peces.removeAllElements();
		piranhas.removeAllElements();
		shark = null;

	}

	// Canvas communication

	@Override
	protected void setCanvasSize(int width, int height) {
		img.setCanvas(width, height);
		// if (width > 1000) {
		// TODO: es una tablet, redirigir al link del market de la tablet.
		// }
	}

	// Events

	public synchronized void onTapDown(int x, int y) {

		if (isControllingGato && btnDown.isVisible() && btnDown.isTapped(x, y))
			gato.goDown();

		else if (btnSkip.isVisible())
			if (btnSkip.isTapped(x, y))
				setScreen(SCREEN_GAME_READY);
		
		
	}

	public synchronized void onTapUp() {
		gato.goUp();
	}

	public synchronized void turn(float pitch) {
		gato.timon(pitch);
	}

	public synchronized boolean exit() {
		if (ttoast.getSelected()==7 && ttoast.isEnabled())
			return true;
		
		ttoast.start(Text.TEXT_FAST, 7, 1);
		
		return false;
	}
	
}
