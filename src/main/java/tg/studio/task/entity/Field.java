package tg.studio.task.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
public class Field {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "field_id")
    private String field_id;
    @Column(name = "row")
    private int row;

    @Column(name = "col")
    private int col;

    @Column(name = "value")
    private String value;

    @Column(name = "is_mine")
    private boolean isMine;

    @Column(name = "behind_mines_count")
    private int behindMinesCount;

    @ManyToOne
    private Game game;


    public Field(int row, int col, boolean isMine, Game game) {
        this.row = row;
        this.col = col;
        this.game = game;
        this.isMine = isMine;
        this.value = " ";
    }


    public Field() {

    }

    public boolean isOpen(){
        return !Objects.equals(value, " ");
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getBehindMinesCount() {
        return behindMinesCount;
    }

    public void setBehindMinesCount(int behindMinesCount) {
        this.behindMinesCount = behindMinesCount;
    }
}
