package paztechnologies.com.meribus.model;

/**
 * Created by Admin on 5/31/2017.
 */

public class Pay_Per_Day_Model {

    String select,s_no,date,total_seat,trip_type,seats,per_seat_total;

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getPer_seat_total() {
        return per_seat_total;
    }

    public void setPer_seat_total(String per_seat_total) {
        this.per_seat_total = per_seat_total;
    }
}
