package br.com.dio.model;

public class Space {

    private Integer actual;
    private final boolean fixed;

    public Space(Integer initialValue, boolean fixed) {
        this.fixed = fixed;
        if (fixed) {
            this.actual = initialValue;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public boolean setActual(final Integer actual) {
        if (fixed) {
            return false;
        }
        this.actual = actual;
        return true;
    }

    public void clearSpace(){
        if (!isFixed()) {
            this.actual = null;
        }
    }

    public boolean isFixed() {
        return fixed;
    }
}
