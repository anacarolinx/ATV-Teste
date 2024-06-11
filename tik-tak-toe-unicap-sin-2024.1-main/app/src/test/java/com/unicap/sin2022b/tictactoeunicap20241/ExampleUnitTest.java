package com.unicap.sin2022b.tictactoeunicap20241;

import org.junit.Test;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.unicap.sin2022b.tictactoeunicap20241.R;
import com.unicap.sin2022b.tictactoeunicap20241.Service.GameManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private GameActivity gameActivity;
    private GameManager mockGameManager;

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), GameActivity.class);
        intent.putExtra("gameId", "testGameId");
        gameActivity = new GameActivity();
        gameActivity.setIntent(intent);

        mockGameManager = Mockito.mock(GameManager.class);
        GameManager.setInstance(mockGameManager);
    }

    @Test
    public void testOnCreate() {
        gameActivity.onCreate(null);

        // Verifica se os componentes foram inicializados
        assertNotNull(gameActivity.findViewById(R.id.playerOneImage));
        assertNotNull(gameActivity.findViewById(R.id.playerTwoImage));
        assertNotNull(gameActivity.findViewById(R.id.playerOneName));
        assertNotNull(gameActivity.findViewById(R.id.playerTwoName));

        // Verifica se o jogo foi inicializado corretamente
        verify(mockGameManager).initializeGame(eq("testGameId"), any(GameManager.GameManagerCallback.class));
    }

    @Test
    public void testOnGameStateLoaded() {
        gameActivity.onCreate(null);
        when(mockGameManager.getPlayerOneName()).thenReturn("Player One");
        when(mockGameManager.getPlayerTwoName()).thenReturn("Player Two");
        when(mockGameManager.getPlayerOneProfile()).thenReturn("http://example.com/playerOne.png");
        when(mockGameManager.getPlayerTwoProfile()).thenReturn("http://example.com/playerTwo.png");
        when(mockGameManager.getBoardState()).thenReturn(new String[3][3]);

        gameActivity.onGameStateLoaded();

        // Verifica se os nomes dos jogadores foram atualizados corretamente
        TextView playerOneName = gameActivity.findViewById(R.id.playerOneName);
        assertEquals("Player One", playerOneName.getText().toString());

        TextView playerTwoName = gameActivity.findViewById(R.id.playerTwoName);
        assertEquals("Player Two", playerTwoName.getText().toString());

        // Verifica se as imagens dos jogadores foram carregadas
        ImageView playerOneImage = gameActivity.findViewById(R.id.playerOneImage);
        ImageView playerTwoImage = gameActivity.findViewById(R.id.playerTwoImage);
        assertNotNull(playerOneImage.getDrawable());
        assertNotNull(playerTwoImage.getDrawable());
    }

    @Test
    public void testOnGameEnd() {
        gameActivity.onCreate(null);

        gameActivity.onGameEnd("Player One wins!");

        // Verifica se o diálogo de resultado foi exibido
        // Você pode precisar de um método público ou de outra forma de verificar o estado da UI
        // dependendo da implementação da sua ResultDialog.
        // Neste exemplo, estamos verificando a mensagem do callback.
        assertEquals("Player One wins!", gameActivity.getResultDialogMessage());
    }

    @Test
    public void testMakeMove() {
        gameActivity.onCreate(null);
        when(mockGameManager.isGameEnded()).thenReturn(false);
        when(mockGameManager.isPlayerOneTurn()).thenReturn(true);
        when(mockGameManager.getBoardState()).thenReturn(new String[3][3]);

        int index = 0; // Top-left corner of the board
        gameActivity.makeMove(index);

        // Verifica se a jogada foi registrada corretamente
        verify(mockGameManager).makeMove(0, 0);
        // Verifica se o método updateBoardCell foi chamado corretamente
        // Você pode precisar de um método público ou de outra forma de verificar o estado da UI
    }

    @Test
    public void testUpdateBoardCell() {
        gameActivity.onCreate(null);

        int index = 0; // Top-left corner of the board
        gameActivity.updateBoardCell(index, "X");

        ImageView boardCell = gameActivity.findViewById(R.id.image1);
        assertEquals(R.drawable.ic_xicon, boardCell.getDrawable());

        gameActivity.updateBoardCell(index, "O");
        assertEquals(R.drawable.ic_oicon, boardCell.getDrawable());

        gameActivity.updateBoardCell(index, null);
        assertEquals(0, boardCell.getDrawable());
    }

    @Test
    public void testApplyPlayerTurn() {
        gameActivity.onCreate(null);
        when(mockGameManager.isPlayerOneTurn()).thenReturn(true);

        gameActivity.applyPlayerTurn();

        // Verifica se a borda preta está no jogador um
        View playerOneLayout = gameActivity.findViewById(R.id.playerOneLayout);
        assertEquals(R.drawable.black_border, playerOneLayout.getBackground());

        when(mockGameManager.isPlayerOneTurn()).thenReturn(false);
        gameActivity.applyPlayerTurn();

        // Verifica se a borda preta está no jogador dois
        View playerTwoLayout = gameActivity.findViewById(R.id.playerTwoLayout);
        assertEquals(R.drawable.black_border, playerTwoLayout.getBackground());
    }
}