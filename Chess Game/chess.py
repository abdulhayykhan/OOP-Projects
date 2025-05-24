import tkinter as tk
from tkinter import messagebox

# Unicode symbols for chess pieces
UNICODE_PIECES = {
    'K': '\u2654',  # White King
    'Q': '\u2655',
    'R': '\u2656',
    'B': '\u2657',
    'N': '\u2658',
    'P': '\u2659',
    'k': '\u265A',  # Black King
    'q': '\u265B',
    'r': '\u265C',
    'b': '\u265D',
    'n': '\u265E',
    'p': '\u265F'
}

class Piece:
    def __init__(self, color):
        self.color = color
        self.name = "Piece"

    def possible_moves(self, position, board):
        return []

    def symbol(self):
        return '?'

    def __str__(self):
        s = self.symbol()
        return s if self.color == 'white' else s.lower()


class King(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "King"

    def symbol(self):
        return 'K'

    def possible_moves(self, pos, board):
        moves = []
        directions = [(1,0), (-1,0), (0,1), (0,-1), (1,1), (1,-1), (-1,1), (-1,-1)]
        for d in directions:
            r, c = pos[0] + d[0], pos[1] + d[1]
            if 0 <= r < 8 and 0 <= c < 8:
                if board.is_empty_or_enemy((r,c), self.color):
                    moves.append((r,c))
        return moves


class Queen(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "Queen"

    def symbol(self):
        return 'Q'

    def possible_moves(self, pos, board):
        return Rook(self.color).possible_moves(pos, board) + Bishop(self.color).possible_moves(pos, board)


class Rook(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "Rook"

    def symbol(self):
        return 'R'

    def possible_moves(self, pos, board):
        moves = []
        directions = [(1,0), (-1,0), (0,1), (0,-1)]
        for d in directions:
            r, c = pos
            while True:
                r += d[0]
                c += d[1]
                if 0 <= r < 8 and 0 <= c < 8:
                    if board.is_empty((r,c)):
                        moves.append((r,c))
                    elif board.is_enemy((r,c), self.color):
                        moves.append((r,c))
                        break
                    else:
                        break
                else:
                    break
        return moves


class Bishop(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "Bishop"

    def symbol(self):
        return 'B'

    def possible_moves(self, pos, board):
        moves = []
        directions = [(1,1), (1,-1), (-1,1), (-1,-1)]
        for d in directions:
            r, c = pos
            while True:
                r += d[0]
                c += d[1]
                if 0 <= r < 8 and 0 <= c < 8:
                    if board.is_empty((r,c)):
                        moves.append((r,c))
                    elif board.is_enemy((r,c), self.color):
                        moves.append((r,c))
                        break
                    else:
                        break
                else:
                    break
        return moves


class Knight(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "Knight"

    def symbol(self):
        return 'N'

    def possible_moves(self, pos, board):
        moves = []
        jumps = [(2,1),(2,-1),(-2,1),(-2,-1),(1,2),(1,-2),(-1,2),(-1,-2)]
        for j in jumps:
            r, c = pos[0] + j[0], pos[1] + j[1]
            if 0 <= r < 8 and 0 <= c < 8:
                if board.is_empty_or_enemy((r,c), self.color):
                    moves.append((r,c))
        return moves


class Pawn(Piece):
    def __init__(self, color):
        super().__init__(color)
        self.name = "Pawn"

    def symbol(self):
        return 'P'

    def possible_moves(self, pos, board):
        moves = []
        direction = -1 if self.color == 'white' else 1
        start_row = 6 if self.color == 'white' else 1

        # One step forward
        front = (pos[0] + direction, pos[1])
        if 0 <= front[0] < 8 and board.is_empty(front):
            moves.append(front)

            # Two steps forward from start row
            double_front = (pos[0] + 2*direction, pos[1])
            if pos[0] == start_row and board.is_empty(double_front):
                moves.append(double_front)

        # Captures
        for dc in [-1, 1]:
            diag = (pos[0] + direction, pos[1] + dc)
            if 0 <= diag[0] < 8 and 0 <= diag[1] < 8:
                if board.is_enemy(diag, self.color):
                    moves.append(diag)

        return moves


class Board:
    def __init__(self):
        self.board = [[None]*8 for _ in range(8)]
        self.setup_pieces()

    def setup_pieces(self):
        for i in range(8):
            self.board[6][i] = Pawn('white')
            self.board[1][i] = Pawn('black')

        self.board[7][0] = Rook('white')
        self.board[7][7] = Rook('white')
        self.board[0][0] = Rook('black')
        self.board[0][7] = Rook('black')

        self.board[7][1] = Knight('white')
        self.board[7][6] = Knight('white')
        self.board[0][1] = Knight('black')
        self.board[0][6] = Knight('black')

        self.board[7][2] = Bishop('white')
        self.board[7][5] = Bishop('white')
        self.board[0][2] = Bishop('black')
        self.board[0][5] = Bishop('black')

        self.board[7][3] = Queen('white')
        self.board[0][3] = Queen('black')

        self.board[7][4] = King('white')
        self.board[0][4] = King('black')

    def is_empty(self, pos):
        r, c = pos
        return self.board[r][c] is None

    def is_enemy(self, pos, color):
        r, c = pos
        piece = self.board[r][c]
        return piece is not None and piece.color != color

    def is_empty_or_enemy(self, pos, color):
        return self.is_empty(pos) or self.is_enemy(pos, color)

    def move_piece(self, start, end):
        piece = self.board[start[0]][start[1]]
        self.board[end[0]][end[1]] = piece
        self.board[start[0]][start[1]] = None

    def find_king(self, color):
        for r in range(8):
            for c in range(8):
                piece = self.board[r][c]
                if piece and isinstance(piece, King) and piece.color == color:
                    return (r,c)
        return None

    def in_check(self, color):
        king_pos = self.find_king(color)
        if not king_pos:
            return True  # King missing = check

        for r in range(8):
            for c in range(8):
                piece = self.board[r][c]
                if piece and piece.color != color:
                    moves = piece.possible_moves((r,c), self)
                    if king_pos in moves:
                        return True
        return False

    def valid_move(self, start, end, color):
        piece = self.board[start[0]][start[1]]
        if not piece or piece.color != color:
            return False
        if end not in piece.possible_moves(start, self):
            return False

        # Simulate move to check for self-check
        captured = self.board[end[0]][end[1]]
        self.board[end[0]][end[1]] = piece
        self.board[start[0]][start[1]] = None
        in_check = self.in_check(color)
        # Undo move
        self.board[start[0]][start[1]] = piece
        self.board[end[0]][end[1]] = captured

        return not in_check

    def has_moves(self, color):
        for r in range(8):
            for c in range(8):
                piece = self.board[r][c]
                if piece and piece.color == color:
                    for move in piece.possible_moves((r,c), self):
                        if self.valid_move((r,c), move, color):
                            return True
        return False


class ChessGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Chess Game")
        self.board = Board()
        self.turn = 'white'
        self.selected = None  # Selected square (row, col)
        self.buttons = [[None]*8 for _ in range(8)]

        self.create_board()
        self.update_board()

    def create_board(self):
        for r in range(8):
            for c in range(8):
                color = 'white' if (r + c) % 2 == 0 else 'gray'
                btn = tk.Button(self.root, width=4, height=2, font=('Arial', 24),
                                bg=color, fg='black',
                                command=lambda r=r, c=c: self.click_square(r, c))
                btn.grid(row=r, column=c)
                self.buttons[r][c] = btn

    def update_board(self):
        for r in range(8):
            for c in range(8):
                piece = self.board.board[r][c]
                btn = self.buttons[r][c]
                if piece:
                    sym = UNICODE_PIECES[piece.symbol()] if piece.color == 'white' else UNICODE_PIECES[piece.symbol().lower()]
                    btn.config(text=sym)
                else:
                    btn.config(text='')

                # Reset button bg color
                bg_color = 'white' if (r + c) % 2 == 0 else 'gray'
                btn.config(bg=bg_color)

        # Highlight selected square if any
        if self.selected:
            r, c = self.selected
            self.buttons[r][c].config(bg='yellow')

    def click_square(self, r, c):
        if self.selected is None:
            piece = self.board.board[r][c]
            if piece and piece.color == self.turn:
                self.selected = (r,c)
                self.update_board()
        else:
            start = self.selected
            end = (r,c)
            if self.board.valid_move(start, end, self.turn):
                self.board.move_piece(start, end)
                if self.board.in_check('black' if self.turn == 'white' else 'white'):
                    if not self.board.has_moves('black' if self.turn == 'white' else 'white'):
                        messagebox.showinfo("Game Over", f"Checkmate! {self.turn.capitalize()} wins!")
                        self.root.quit()
                    else:
                        messagebox.showinfo("Check", f"{'Black' if self.turn == 'white' else 'White'} is in check!")
                else:
                    if not self.board.has_moves('black' if self.turn == 'white' else 'white'):
                        messagebox.showinfo("Game Over", "Stalemate! It's a draw.")
                        self.root.quit()
                self.turn = 'black' if self.turn == 'white' else 'white'
            else:
                messagebox.showwarning("Invalid Move", "That move is not valid!")

            self.selected = None
            self.update_board()


if __name__ == "__main__":
    root = tk.Tk()
    ChessGUI(root)
    root.mainloop()

