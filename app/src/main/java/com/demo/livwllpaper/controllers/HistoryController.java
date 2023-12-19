package com.demo.livwllpaper.controllers;

import android.graphics.Bitmap;
import android.util.Log;

import com.demo.livwllpaper.beans.Point;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;


public class HistoryController {
    private static final int TAMANHO_MAX_PILHA_INIT = 30;
    private static HistoryController instance;
    private int Current_Group_Id = 0;
    private int idGroupMax = 30;
    private Stack<Object_History> Stack_Undo = new Stack<>();
    private Stack<Object_History> Stack_Redo = new Stack<>();

    
    public static class Object_History {
        boolean adicionado;
        int idGroup;
        Bitmap mask;
        Point ponto;

        public Object_History(int i, Point lPMLWPoint, boolean z) {
            this.idGroup = -1;
            this.idGroup = i;
            this.ponto = lPMLWPoint;
            this.adicionado = z;
        }

        public Object_History(int i, Bitmap bitmap, boolean z) {
            this.idGroup = -1;
            this.mask = bitmap;
            this.adicionado = z;
            this.idGroup = i;
        }

        public boolean isMascara() {
            return this.mask != null;
        }

        public boolean isAdicionado() {
            return this.adicionado;
        }

        public Bitmap getMask() {
            return this.mask;
        }

        public Point getPonto() {
            return this.ponto;
        }
    }

    private HistoryController() {
        clear();
    }

    public void clear() {
        this.Current_Group_Id = 0;
        this.Stack_Redo.clear();
        this.Stack_Undo.clear();
        this.idGroupMax = 30;
    }

    private void check_Limit() {
        try {
            int i = this.Current_Group_Id;
            int i2 = this.idGroupMax;
            if (i >= i2) {
                this.idGroupMax = i2 + 1;
                do {
                    try {
                    } catch (Exception unused) {
                        return;
                    }
                } while (this.Stack_Undo.get(0).idGroup == this.Stack_Undo.remove(0).idGroup);
            }
        }catch (Exception e){
            Log.e("TAG", "check_Limit: " +e);

        }

    }

    public void add_history(Point lPMLWPoint, boolean z) {
        this.Stack_Undo.push(new Object_History(this.Current_Group_Id, lPMLWPoint, z));
        this.Stack_Redo.clear();
        check_Limit();
        this.Current_Group_Id++;
    }

    public void remove_history() {
        this.Stack_Undo.pop();
    }

    public void add_history(List<Point> list, boolean z) {
        for (Point lPMLWPoint : list) {
            this.Stack_Undo.push(new Object_History(this.Current_Group_Id, lPMLWPoint, z));
        }
        this.Stack_Redo.clear();
        check_Limit();
        this.Current_Group_Id++;
    }

    public void add_history(Bitmap bitmap, boolean z) {
        this.Stack_Undo.push(new Object_History(this.Current_Group_Id, bitmap, z));
        this.Stack_Redo.clear();
        check_Limit();
        this.Current_Group_Id++;
    }

    public boolean temDesfazer() {
        return !this.Stack_Undo.isEmpty();
    }

    public boolean temRefazer() {
        return !this.Stack_Redo.isEmpty();
    }

    public List<Object_History> desfazer() {
        Object_History object_History;
        Object_History pop;
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        if (!this.Stack_Undo.isEmpty()) {
            do {
                object_History = null;
                pop = this.Stack_Undo.pop();
                copyOnWriteArrayList.add(pop);
                this.Current_Group_Id = pop.idGroup;
                this.Stack_Redo.push(pop);
                if (!this.Stack_Undo.isEmpty()) {
                    object_History = this.Stack_Undo.peek();
                }
                if (object_History == null) {
                    break;
                }
            } while (object_History.idGroup == pop.idGroup);
        }
        return copyOnWriteArrayList;
    }

    public List<Object_History> refazer() {
        Object_History object_History;
        Object_History pop;
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        if (!this.Stack_Redo.isEmpty()) {
            do {
                object_History = null;
                pop = this.Stack_Redo.pop();
                copyOnWriteArrayList.add(pop);
                this.Current_Group_Id = pop.idGroup + 1;
                this.Stack_Undo.push(pop);
                if (!this.Stack_Redo.isEmpty()) {
                    object_History = this.Stack_Redo.peek();
                }
                if (object_History == null) {
                    break;
                }
            } while (object_History.idGroup == pop.idGroup);
        }
        return copyOnWriteArrayList;
    }

    public static HistoryController getInstance() {
        if (instance == null) {
            instance = new HistoryController();
        }
        return instance;
    }
}
