public class Field {

    /** Default count of row and column */
    public static final int DEFAULT_COUNT = 19; // размер поля

    private int mCount; //текущее количество строк и столбцов на поле
    private Cell[][] mCells; //двумерный массив объектов типа Cell, представляющих ячейки поля.

    public Field(int aCount) {
        //заполняем все ячейки как пустые
        mCount = aCount;
        mCells = new Cell[mCount][mCount];
        for (int i = 0; i < mCount; i++)
            for (int j = 0; j < mCount; j++)
                mCells[i][j] = new Cell(Cell.EMPTY);
    }

    public int getCount() {
        return mCount;
    } // Возвращает текущее количество строк и столбцов на поле

    public int getCellType(int aX, int aY) {
        if (aX < 0 || aX >= mCount || aY < 0 || aY >= mCount) return Cell.NOT_CELL.getType();
        return mCells[aX][aY].getType();
    } // Возвращает тип ячейки по заданным координатам.
      // Если координаты выходят за пределы поля, возвращается тип NOT_CELL

    public boolean setCellType(int aX, int aY, int aType) {
        if (aX < 0 || aX >= mCount || aY < 0 || aY >= mCount) return false;
        if (mCells[aX][aY].getType() != Cell.EMPTY) return false;
        mCells[aX][aY].setType(aType);
        return true;
    }
    //Устанавливает тип ячейки по заданным координатам, если ячейка пуста (EMPTY).
    // Возвращает true, если установка прошла успешно, и false в противном случае.
}
