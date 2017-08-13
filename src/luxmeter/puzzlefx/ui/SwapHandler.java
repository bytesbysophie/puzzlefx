package luxmeter.puzzlefx.ui;

import java.util.Collections;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import luxmeter.puzzlefx.model.Piece;

/**
 * Handles user's wish to swap two puzzle pieces.
 */
class SwapHandler implements EventHandler<MouseEvent> {
    private Controller controller;
    public Piece previousClickedPiece;

    public SwapHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent event) {
        Piece piece = findClickedPiece(event);
        boolean twoPiecesChosenToSwap = piece != null && previousClickedPiece != null;
        if (twoPiecesChosenToSwap) {
            swapPieces(piece);
            controller.drawShuffledPieces();
            previousClickedPiece = null;
        }
        else {
            previousClickedPiece = piece;
        }
    }

    private void swapPieces(Piece piece) {
        if (piece != previousClickedPiece) {
            List<Piece> shuffledPieces = controller.getShuffledPieces();
            List<Piece> piecesInNormalOrder = controller.getPiecesInNormalOrder();
            int indexOfPrevPiece = piecesInNormalOrder.indexOf(previousClickedPiece);
            int indexOfPiece = piecesInNormalOrder.indexOf(piece);
            Collections.swap(shuffledPieces, indexOfPrevPiece, indexOfPiece);
        }
    }

    private Piece findClickedPiece(MouseEvent event) {
        for (Piece piece : controller.getPiecesInNormalOrder()) {
            Rectangle2D pieceRect = new Rectangle2D(piece.getXPos(), piece.getYPos(), piece.getWidth(), piece.getHeight());
            if (pieceRect.contains(event.getSceneX(), event.getSceneY())) {
                return piece;
            }
        }
        return null;
    }
}
