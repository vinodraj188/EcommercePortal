
package com.mycompany.ecomproj.servlet;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.ecomproj.dao.ProductDAO;
import com.mycompany.ecomproj.impl.ProductDAOImpl;
import com.mycompany.ecomproj.model.Product;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@MultipartConfig(maxRequestSize = 25*1024*1024,maxFileSize = 5*1024*1024)
public class UpdateProductToDB extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
         String name = request.getParameter("name");
        String description = request.getParameter("description");
        String pr = request.getParameter("price");
        double price = Double.valueOf(pr);
        
        int qty = Integer.parseInt(request.getParameter("qty"));
        String category = request.getParameter("category");
        
        Part part = request.getPart("imagePath");
        
        System.out.println("File Size"+part.getSize());
        System.out.println("File Name"+part.getSubmittedFileName());
        System.out.println("File Directory"+request.getRealPath(""));
        
        FileOutputStream fos = new FileOutputStream(request.getRealPath("")+"/"+part.getSubmittedFileName());
        byte[] b = new byte[part.getInputStream().available()];
        part.getInputStream().read(b);
        fos.write(b);
        fos.close();
        
        File f = new File(request.getRealPath("")+"/"+part.getSubmittedFileName());
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "vinod1232",
                "api_key", "624137223589493",
                "api_secret", "XSMNdx3DUOakoX2S63bqzQMlNt8"));

    
         Map uploadResult = cloudinary.uploader().upload(f, ObjectUtils.emptyMap());
            String imagePath = (String)uploadResult.get("secure_url");
        
        
      
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setImagePath(imagePath);
        p.setQty(qty);
        p.setCategory(category);
        
        ProductDAO pdao = new ProductDAOImpl();
        pdao.update(p);
        response.sendRedirect("Product/ViewProduct.jsp");
    }
}
