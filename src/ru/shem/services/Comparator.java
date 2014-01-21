package ru.shem.services;

import com.alex.map.Booking;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 15.01.14
 *
 * В этом классе описсывается логика по которой сортируется лист истории
 */
class Comparator implements java.util.Comparator<Booking> {
    private String sortType;

    private static final String STATUS_ACTUAL = "actual";
    private static final String STATUS_PAUSE = "pause";

    /**
     *  Типы сортировки:
     *  1) THROUGH_SORT_TYPE - сквозная сортировка, внизу не активные заказы, а на верху активные.
     *                                      Та что предложил сделать Лёша.
     *  2) TIMELINE_SORT_TYPE - сортировка по принципу единой временной шкалы.
     *                                      Та что предложил Юра.
     */
    private static final String THROUGH_SORT_TYPE = "through";
    private static final String TIMELINE_SORT_TYPE = "timeline";

    public Comparator(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(Booking lhs, Booking rhs) {

        int result = 0;

        // сортировка типа единой временой шкалы
        if(sortType.equals(TIMELINE_SORT_TYPE)) {
            if(lhs.getFromDate().compareTo(rhs.getFromDate()) == 0) {
                result = -lhs.getFromTime().compareTo(rhs.getFromTime());
            } else {
                result = -lhs.getFromDate().compareTo(rhs.getFromDate());
            }
        }
        // сквозная сортировка
        if(sortType.equals(THROUGH_SORT_TYPE)) {

            // сначало сортируем всё в единую временную шкалу
            if(lhs.getFromDate().compareTo(rhs.getFromDate()) == 0) {
                result = -lhs.getFromTime().compareTo(rhs.getFromTime());
            } else {
                result = -lhs.getFromDate().compareTo(rhs.getFromDate());
            }

            // после получения единой временной шкалы, отсортировываем только активные заказы в обратном порядке
            if(lhs.getStatus().equals(STATUS_ACTUAL) && rhs.getStatus().equals(STATUS_ACTUAL)) {
                if(lhs.getFromDate().compareTo(rhs.getFromDate()) == 0) {
                    result = lhs.getFromTime().compareTo(rhs.getFromTime());
                } else {
                    result = lhs.getFromDate().compareTo(rhs.getFromDate());
                }
            }
            if(lhs.getStatus().equals(STATUS_ACTUAL) && rhs.getStatus().equals(STATUS_PAUSE)) {
                result = -1;
            }
            if(lhs.getStatus().equals(STATUS_PAUSE) && rhs.getStatus().equals(STATUS_ACTUAL)) {
                result = 1;
            }
        }

        return result;
    }
}