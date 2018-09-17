package com.calak.jemmy.spiro.Data.Online;

public class Config {

    public static String APIKEY ="MTFhM2FmMzBhZGI1ODAwNTk1ODUxODVlNjRjOGM4ZjczOTg3MTI1NDpmZWM0MzczOGE4OTQ0YjY1OTZmMTY3NmI3NzZmYzVkZGM3NmE3YThh";

    public static String BASEUSRL="http://parkir.genspira.id/api/";
    public static String getDataParkir=BASEUSRL+"lokasi/list";
    public static Object login = BASEUSRL + "account/login";
    public static Object register = BASEUSRL + "account/register";
    public static String reservasi = BASEUSRL+"reservasi/booking";
    public static String batalreservasi = BASEUSRL + "reservasi/cancel";
    public static String getSaldo=BASEUSRL+"account/saldo";
}