package tg.studio.task.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import tg.studio.task.entity.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FieldProcessor {
    public List<Field> fields;
    private int height;
    private int width;

    public boolean hasFieldInPosition(int row, int col) {
        if (row >= height || col >= width || row < 0 || col < 0)
            return false;
        int index = row * width + col;
        return index < height * width && index >= 0;
    }

    public Field getFieldByPos(int row, int col) {
        if (row >= height)
            throw new IllegalArgumentException("row >= height");
        if (col >= width)
            throw new IllegalArgumentException("col >= width");

        int index = row * width + col;
        return fields.get(index);
    }


    // Открываем ячейку, рядом с которой нет мин
    public Field openFieldBehindMinesZero(Field field) {
        if (field.isOpen())
            return field;
        field.setValue(String.valueOf(field.getBehindMinesCount()));

        var behindFields = getBehindFields(field);

        // Просто открывам ячейки, если рядом с ними есть мины
        behindFields.stream().filter(f -> f.getBehindMinesCount() > 0)
                .forEach(f -> turnField(f.getRow(), f.getCol()));

        // Рекурсивно открывам ячейки, если рядом с ними нет мин openFieldBehindMinesZero
        behindFields.stream().filter(f -> f.getBehindMinesCount() == 0)
                .forEach(this::openFieldBehindMinesZero);


        return field;
    }


    // Открываем ячейку
    public Field turnField(int row, int col) {
        var field = getFieldByPos(row, col);
        // Ячейка уже открыта
        if (field.isOpen())
            return field;

        // Ячейка - мина
        if (field.isMine()) {
            field.setValue("M");
            return field;
        }

        // Ячейка - пустая
        if (field.getBehindMinesCount() > 0) {
            field.setValue(String.valueOf(field.getBehindMinesCount()));
            return field;
        }
        // Ячейка - пустая и рядом нет мин
        openFieldBehindMinesZero(field);

        return field;
    }

    public Field turnField(Field field) {
        return turnField(field.getRow(), field.getCol());
    }

    public void openFieldLose(Field field){
        if (field.isOpen())
            return;

        // Ячейка - мина
        if (field.isMine()) {
            field.setValue("X");
            return;
        }
        // Ячейка - пустая
        field.setValue(String.valueOf(field.getBehindMinesCount()));
    }
    public void openFieldWin(Field field) {
        if (field.isOpen())
            return;

        // Ячейка - мина
        if (field.isMine()) {
            field.setValue("M");
            return;
        }
        // Ячейка - пустая
        field.setValue(String.valueOf(field.getBehindMinesCount()));
    }


    public List<Field> getBehindFields(Field field) {
        return getBehindFields(field.getRow(), field.getCol());
    }

    public List<Field> getBehindFields(int row, int col) {
        var res = new ArrayList<Field>();
        int rowUp = row + 1;
        int rowDown = row - 1;
        int colRight = col + 1;
        int colLeft = col - 1;

        // up-left
        if (hasFieldInPosition(rowUp, colLeft))
            res.add(getFieldByPos(rowUp, colLeft));

        // up
        if (hasFieldInPosition(rowUp, col))
            res.add(getFieldByPos(rowUp, col));

        // up-right
        if (hasFieldInPosition(rowUp, colRight))
            res.add(getFieldByPos(rowUp, colRight));

        // right
        if (hasFieldInPosition(row, colRight))
            res.add(getFieldByPos(row, colRight));

        // down-right
        if (hasFieldInPosition(rowDown, colRight))
            res.add(getFieldByPos(rowDown, colRight));

        // down
        if (hasFieldInPosition(rowDown, col))
            res.add(getFieldByPos(rowDown, col));

        // down-left
        if (hasFieldInPosition(rowDown, colLeft))
            res.add(getFieldByPos(rowDown, colLeft));

        // left
        if (hasFieldInPosition(row, colLeft))
            res.add(getFieldByPos(row, colLeft));

        return res;
    }


}
