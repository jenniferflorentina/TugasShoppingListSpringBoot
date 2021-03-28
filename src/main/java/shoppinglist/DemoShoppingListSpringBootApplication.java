package shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner
{
    @Autowired
    private DaftarBelanjaRepo repo;

    @Autowired
    private DaftarBelanjaDetilRepo detilRepo;

    public static void main(String[] args)
    {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Membaca Semua Record Daftar Belanja");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println(db.getId()+" . "+db.getJudul());
            List<DaftarBelanjaDetil> daftarBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : daftarBarang) {
                System.out.println("\t" + barang.getNoUrut()+ ". "+ barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        }

        //1
        System.out.println("\nMembaca Sebuah Objek Daftar Belanja Berdasarkan ID");
        Scanner keyb = new Scanner(System.in);
        System.out.print("Masukkan ID : ");
        long id = Long.parseLong(keyb.nextLine());
        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println(db.getId()+" . "+db.getJudul());
            List<DaftarBelanjaDetil> daftarBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : daftarBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        } else {
            System.out.println("ID tidak ditemukan!");
        }

        //2
        System.out.println("\nMencari daftar DaftarBelanja berdasarkan kemiripan string judul yg diberikan");
        System.out.print("Masukkan judul : ");
        String judul = keyb.nextLine();
        List<DaftarBelanja> list_db = repo.findByJudulContainingIgnoreCase(judul);
        if (list_db.size() > 0) {
            for (DaftarBelanja db : list_db) {
                System.out.println(db.getId()+" . "+db.getJudul());
                List<DaftarBelanjaDetil> daftarBarang = db.getDaftarBarang();
                for (DaftarBelanjaDetil barang : daftarBarang) {
                    System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
                }
            }
        } else {
            System.out.println("Judul tidak ditemukan!");
        }

        //3
        System.out.println("\nMenyimpan sebuah objek DaftarBelanja ke tabel database");
        System.out.print("Masukkan judul : ");
        judul = keyb.nextLine();
        DaftarBelanja in_db = new DaftarBelanja();
        in_db.setJudul(judul);
        in_db.setTanggal(LocalDateTime.now());
        in_db = repo.save(in_db);
        List<DaftarBelanjaDetil> detils = new LinkedList();
        int counter = 1;
        String namaBarang = "";
        while(true){
            System.out.println("Jika ingin stop input 0 ");
            System.out.print("Nama Barang :");
            namaBarang = keyb.nextLine();
            if(namaBarang.compareTo("0")==0){
                break;
            }
            System.out.print("Banyak :");
            float jml = Float.parseFloat(keyb.nextLine());
            System.out.print("Satuan :");
            String satuan = keyb.nextLine();
            System.out.print("Memo :");
            String memo = keyb.nextLine();
            DaftarBelanjaDetil detil = new DaftarBelanjaDetil();
            detil.setNoUrut(counter);
            detil.setNamaBarang(namaBarang);
            detil.setByk(jml);
            detil.setSatuan(satuan);
            detil.setMemo(memo);
            detils.add(detil);
            counter++;
        }
        for (DaftarBelanjaDetil barang : detils) {
            barang.setInduk(in_db);
            detilRepo.save(barang);
        }
        optDB = repo.findById(in_db.getId());
        in_db = optDB.get();
        System.out.println("INSERTED");
        System.out.println(in_db.getId()+" . "+in_db.getJudul());
        List<DaftarBelanjaDetil> daftarBarang = in_db.getDaftarBarang();
        for (DaftarBelanjaDetil barang : daftarBarang) {
            System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
        }

        //4
        System.out.println("\nMengupdate sebuah objek DaftarBelanja ke tabel database");
        System.out.print("Masukkan ID : ");
        id = Long.parseLong(keyb.nextLine());
        Optional<DaftarBelanja> update_db = repo.findById(id);
        if (update_db.isPresent()) {
            DaftarBelanja db = update_db.get();
            System.out.print("Masukkan judul : ");
            judul = keyb.nextLine();
            db.setJudul(judul);
            daftarBarang = db.getDaftarBarang();
            for (int i = 0; i < daftarBarang.size(); i++) {
                System.out.print("Nama Barang : "+daftarBarang.get(i).getNamaBarang() + " menjadi ");
                namaBarang = keyb.nextLine();
                System.out.print("Banyak : "+daftarBarang.get(i).getByk()+ " menjadi ");
                float jml = Float.parseFloat(keyb.nextLine());
                System.out.print("Satuan : "+daftarBarang.get(i).getSatuan()+" menjadi ");
                String satuan = keyb.nextLine();
                System.out.print("Memo : "+daftarBarang.get(i).getMemo()+" menjadi ");
                String memo = keyb.nextLine();
                daftarBarang.get(i).setNamaBarang(namaBarang);
                daftarBarang.get(i).setByk(jml);
                daftarBarang.get(i).setSatuan(satuan);
                daftarBarang.get(i).setMemo(memo);
                detilRepo.save(daftarBarang.get(i));
            }
            in_db = repo.save(db);
            optDB = repo.findById(in_db.getId());
            in_db = optDB.get();
            System.out.println("UPDATED");
            System.out.println(in_db.getId()+" . "+in_db.getJudul());
            daftarBarang = in_db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : daftarBarang) {
                System.out.println("\t" +barang.getNoUrut()+ ". "+ barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        } else {
            System.out.println("ID tidak ditemukan!");
        }

        //5
        System.out.println("\nMenghapus objek DaftarBelanja berdasarkan ID yg diberikan");
        System.out.print("Masukkan ID : ");
        id = Long.parseLong(keyb.nextLine());
        optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println(db.getJudul());
            daftarBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : daftarBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
                detilRepo.deleteById(barang.getId());
            }
            repo.deleteById(db.getId());

            all = repo.findAll();
            System.out.println("Sesudah di hapus : ");
            for (DaftarBelanja row : all) {
                System.out.println(row.getJudul());
                daftarBarang = row.getDaftarBarang();
                for (DaftarBelanjaDetil barang : daftarBarang) {
                    System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
                }
            }
        } else {
            System.out.println("ID tidak ditemukan!");
        }
    }
}
