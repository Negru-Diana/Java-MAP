package com.example.retea_de_socializare.utils.paging;

public class Page<E> {
    private Iterable<E> elementsOfPage;
    private int totalNumberOfElements;

    public Page(Iterable<E> elementsOfPage, int totalNumberOfElements) {
        this.elementsOfPage = elementsOfPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public Iterable<E> getElementsOfPage() {
        return elementsOfPage;
    }

    public void setElementsOfPage(Iterable<E> elementsOfPage) {
        this.elementsOfPage = elementsOfPage;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public void setTotalNumberOfElements(int totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
    }
}
