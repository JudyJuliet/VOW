package com.vow.sketchData;

public class VertexStack {
	//定义一个用来储存坐标的栈
		VertexDefine[] m_elements;
	    int m_size = 0;
	  
	    public VertexStack(int len) {
	        m_elements = new VertexDefine[len];
	        m_size = 0;
	    }

	    public VertexStack() {
	        this(100);
	    }

	    //  insert onto stack
	    public void push(VertexDefine element) {
	        m_elements[m_size] = element;
	        m_size++;
	    }

	    // return and remove the top element
	    public VertexDefine pop() {
	        if (!this.isEmpty()) {
	        	VertexDefine obj = m_elements[m_size - 1];
	            m_elements[m_size - 1] = null;
	            m_size--;

	            return obj;
	        } else {
	            return null;
	        }
	    }

	    // return the top element
	    public VertexDefine top() {
	        if (!this.isEmpty()) {
	            return m_elements[m_size - 1];
	        } else {
	            return null;
	        }
	    }

	    // return 1   --> is empty
	    // return 0   --> is not empty
	    public boolean isEmpty() {
	        return this.getSize() == 0;
	    }

	    public int getSize() {
	        return m_size;
	    }
}
