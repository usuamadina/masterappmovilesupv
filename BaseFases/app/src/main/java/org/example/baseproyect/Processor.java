package org.example.baseproyect;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by usuwi on 17/04/2017.
 */

public class Processor {
    private Mat gris;
    private Mat salidaintensidad;
    private Mat salidatrlocal;
    private Mat salidabinarizacion;
    private Mat salidasegmentacion;
    private Mat salidaocr;

    public enum Salida {ENTRADA, INTENSIDAD, OPERADOR_LOCAL, BINARIZACION, SEGMENTACION, RECONOCIMIENTO}

    public enum TipoIntensidad {SIN_PROCESO, LUMINANCIA, AUMENTO_LINEAL_CONSTANTE, EQUALIZ_HISTOGRAMA, ZONAS_ROJAS}

    public enum TipoOperadorLocal {SIN_PROCESO, PASO_BAJO, PASO_ALTO, GRADIENTES}

    public enum TipoBinarizacion {SIN_PROCESO, ADAPTATIVA, MAXIMO}

    public enum TipoSegmentacion {SIN_PROCESO}

    public enum TipoReconocimiento {SIN_PROCESO}

    private Salida mostrarSalida;
    private TipoIntensidad tipoIntensidad;
    private TipoOperadorLocal tipoOperadorLocal;
    private TipoBinarizacion tipoBinarizacion;
    private TipoSegmentacion tipoSegmentacion;
    private TipoReconocimiento tipoReconocimiento;

    public Processor() {
        mostrarSalida = Salida.INTENSIDAD;
        tipoIntensidad = TipoIntensidad.LUMINANCIA;
        tipoOperadorLocal = TipoOperadorLocal.SIN_PROCESO;
        tipoBinarizacion = TipoBinarizacion.SIN_PROCESO;
        tipoSegmentacion = TipoSegmentacion.SIN_PROCESO;
        tipoReconocimiento = TipoReconocimiento.SIN_PROCESO;
        salidaintensidad = new Mat();
        salidatrlocal = new Mat();
        salidabinarizacion = new Mat();
        salidasegmentacion = new Mat();
        salidaocr = new Mat();
        gris = new Mat();
    }

    public Mat process(Mat entrada) {
        if (mostrarSalida == Salida.ENTRADA) {
            return entrada;
        } // Transformación intensidad
        switch (tipoIntensidad) {
            case SIN_PROCESO:
                salidaintensidad = entrada;
                break;
            case LUMINANCIA:
                Imgproc.cvtColor(entrada, salidaintensidad, Imgproc.COLOR_RGBA2GRAY);
                break;
            case AUMENTO_LINEAL_CONSTANTE:
                Imgproc.cvtColor(entrada, gris, Imgproc.COLOR_RGBA2GRAY);
                aumentoLinealConstante(gris);//resultado en salidaintensidad
                break;
            case EQUALIZ_HISTOGRAMA:
                Imgproc.cvtColor(entrada, gris, Imgproc.COLOR_RGBA2GRAY); //Eq. Hist necesita gris
                Imgproc.equalizeHist(gris, salidaintensidad);
                break;
            case ZONAS_ROJAS:
                zonaRoja(entrada); //resultado en salidaintensidad
                break;
            default:
                salidaintensidad = entrada;
        }
        if (mostrarSalida == Salida.INTENSIDAD) {
            return salidaintensidad;
        } // Operador local
        switch (tipoOperadorLocal) {
            case SIN_PROCESO:
                salidatrlocal = salidaintensidad;
                break;
            case PASO_BAJO:
                pasoBajo(salidaintensidad); //resultado en salidatrlocal
                break;
        }
        if (mostrarSalida == Salida.OPERADOR_LOCAL) {
            return salidatrlocal;
        } // Binarización
        switch (tipoBinarizacion) {
            case SIN_PROCESO:
                salidabinarizacion = salidatrlocal;
                break;
            default:
                salidabinarizacion = salidatrlocal;
                break;
        }
        if (mostrarSalida == Salida.BINARIZACION) {
            return salidabinarizacion;
        }
        // Segmentación
        switch (tipoSegmentacion) {
            case SIN_PROCESO:
                salidasegmentacion = salidabinarizacion;
                break;
        }
        if (mostrarSalida == Salida.SEGMENTACION) {
            return salidasegmentacion;
        }
        // Reconocimiento OCR
        switch (tipoReconocimiento) {
            case SIN_PROCESO:
                salidaocr = salidabinarizacion;
                break;
        }
        return salidaocr;
    }

    void zonaRoja(Mat entrada) {
        //Ejemplo para ser rellenada en curso
        salidaintensidad = entrada;
    }

    void aumentoLinealConstante(Mat entrada) {
        //Ejemplo para ser rellenada en curso
        salidaintensidad = entrada;
    }

    void pasoBajo(Mat entrada) {
        //Ejemplo para ser rellenada en curso
        salidatrlocal = entrada;
    }

    public Mat getGris() {
        return gris;
    }

    public void setGris(Mat gris) {
        this.gris = gris;
    }

    public Mat getSalidaintensidad() {
        return salidaintensidad;
    }

    public void setSalidaintensidad(Mat salidaintensidad) {
        this.salidaintensidad = salidaintensidad;
    }

    public Mat getSalidatrlocal() {
        return salidatrlocal;
    }

    public void setSalidatrlocal(Mat salidatrlocal) {
        this.salidatrlocal = salidatrlocal;
    }

    public Mat getSalidabinarizacion() {
        return salidabinarizacion;
    }

    public void setSalidabinarizacion(Mat salidabinarizacion) {
        this.salidabinarizacion = salidabinarizacion;
    }

    public Mat getSalidasegmentacion() {
        return salidasegmentacion;
    }

    public void setSalidasegmentacion(Mat salidasegmentacion) {
        this.salidasegmentacion = salidasegmentacion;
    }

    public Mat getSalidaocr() {
        return salidaocr;
    }

    public void setSalidaocr(Mat salidaocr) {
        this.salidaocr = salidaocr;
    }

    public Salida getMostrarSalida() {
        return mostrarSalida;
    }

    public void setMostrarSalida(Salida mostrarSalida) {
        this.mostrarSalida = mostrarSalida;
    }

    public TipoIntensidad getTipoIntensidad() {
        return tipoIntensidad;
    }

    public void setTipoIntensidad(TipoIntensidad tipoIntensidad) {
        this.tipoIntensidad = tipoIntensidad;
    }

    public TipoOperadorLocal getTipoOperadorLocal() {
        return tipoOperadorLocal;
    }

    public void setTipoOperadorLocal(TipoOperadorLocal tipoOperadorLocal) {
        this.tipoOperadorLocal = tipoOperadorLocal;
    }

    public TipoBinarizacion getTipoBinarizacion() {
        return tipoBinarizacion;
    }

    public void setTipoBinarizacion(TipoBinarizacion tipoBinarizacion) {
        this.tipoBinarizacion = tipoBinarizacion;
    }

    public TipoSegmentacion getTipoSegmentacion() {
        return tipoSegmentacion;
    }

    public void setTipoSegmentacion(TipoSegmentacion tipoSegmentacion) {
        this.tipoSegmentacion = tipoSegmentacion;
    }

    public TipoReconocimiento getTipoReconocimiento() {
        return tipoReconocimiento;
    }

    public void setTipoReconocimiento(TipoReconocimiento tipoReconocimiento) {
        this.tipoReconocimiento = tipoReconocimiento;
    }
}