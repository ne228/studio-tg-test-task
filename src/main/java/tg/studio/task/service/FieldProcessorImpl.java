//package tg.studio.task.service;
//
//import org.springframework.stereotype.Service;
//import tg.studio.task.entity.Field;
//
//import java.util.List;
//
//
//@Service
//public class FieldProcessorImpl implements FieldProcessor {
//    @Override
//    public Field[][] getArrayFromSortedList(List<Field> sortedList, int width, int height) {
//        Field[][] fieldArray = new Field[height][width];
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                int index = height * i + width;
//                fieldArray[i][j] = sortedList.get(index);
//            }
//        }
//        return fieldArray;
//    }
//}
